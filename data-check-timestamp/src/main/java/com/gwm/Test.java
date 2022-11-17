package com.gwm;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONWriter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.json.StrictJsonWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 1>从车端日志当中解析数据信息
 * 2>从数据库端寻找相应的数据信息
 * 3> 将两者的数据作比较，看否一致，不一致就输出时间戳
 */
public class Test {
    public static void main(String[] args) {
        MongoClientURI uri = new MongoClientURI("mongodb://root:123456@39.107.136.198:11101/");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase("mdc-data");
        List<JSONObject> logList = readFromLog();
        List<String> errors = new ArrayList<>();

        //偏差分为数据库无数据和数据不一致两种情况
        List<String> nullErrors = new ArrayList<>();
        List<String> differentErrors = new ArrayList<>();
        //从日志文件和mongodb中读取obj类型的数据，然后做比较
        int total = 0;
        for(JSONObject obj:logList){
            System.out.println("分析数据中"+total++);
            String timestamp = obj.getJSONObject("DevInfo").get("timestamp").toString();
            if(!check(obj,getDocument(db.getCollection("2022-08-16"),timestamp))){
                errors.add(timestamp);
                if(getDocument(db.getCollection("2022-08-16"),timestamp)==null){
                    nullErrors.add(timestamp);}
                else{
                    differentErrors.add(timestamp);
                }
            }
        }

        outPrint(logList,errors,nullErrors,differentErrors);
    }
    public static Object getDocument(MongoCollection<Document> colls,String timestamp){
        Document doc = colls.find(Filters.eq("grabTimestamp",Long.valueOf(timestamp))).first();
        if(doc==null){
            return null;
        }
        Document content = (Document) doc.get("content");
        JsonWriterSettings settings = JsonWriterSettings.builder()
                .outputMode(JsonMode.EXTENDED)
                .int32Converter((Integer value, StrictJsonWriter writer) ->writer.writeNumber(Integer.toString(value)))
                .int64Converter((Long value, StrictJsonWriter writer) ->writer.writeNumber(Long.toString(value)))
                .doubleConverter((Double value, StrictJsonWriter writer)->writer.writeNumber(Double.toString(value)))
                .build();
        return JSONObject.parseObject(content.toJson(settings));
    }
    public static List<JSONObject> readFromLog() {
        try {
            //利用字符流读取文件
            FileReader filereader = new FileReader("E:/Data/upload_data0816-1543-1613.log");
            BufferedReader bufferedReader = new BufferedReader(filereader);

            List<JSONObject> logList = new ArrayList<>();

            String s;//读取一行的字符串s
            while ((s = bufferedReader.readLine()) != null) {

                //如何转化成json字符串呢？
                // 使用阿里的转换器,先转换成json对象，然后获取requestId的值
                JSONObject json = JSONObject.parseObject(s);
                logList.add(json);
            }
            bufferedReader.close();
            return logList;
        } catch (Exception e) {
            System.out.println("获取数据异常···");
            return null;
        }
    }
    public static boolean check(Object obj1, Object obj2) {

        try {
            if(obj2==null){
                return false;
            }
            if (obj1 instanceof JSONObject) {
                JSONObject json1 = JSONObject.parseObject(obj1.toString());
                JSONObject json2 = JSONObject.parseObject(obj2.toString());
                Iterator<String> keys = json1.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (json2.containsKey(key)) {
                        if (!check(json1.get(key), json2.get(key))) {
                            return false;
                        }
                    }
                    else{return false;}
                }
                return true;
            }
            if (obj1 instanceof Integer) {
                Integer i1 = Integer.valueOf(obj1.toString());
                Integer i2 = Integer.valueOf(obj2.toString());
                return i1.compareTo(i2) == 0;
            }
            if(obj1 instanceof Long){
                Long l1 = Long.valueOf(obj1.toString());
                Long l2 = Long.valueOf(obj2.toString());
                return l1.compareTo(l2) == 0;
            }
            if (obj1 instanceof String) {
                return obj1.equals(obj2);
            }
            if (obj1 instanceof Double) {
                Double d1 = Double.valueOf(obj1.toString());
                Double d2 = Double.valueOf(obj2.toString());
                return d1.compareTo(d2) == 0;
            }
            if (obj1 instanceof BigDecimal) {
                Double d1 = Double.valueOf(obj1.toString());
                Double d2 = Double.valueOf(obj2.toString());
                return d1.compareTo(d2) == 0;
            }
            if (obj1 instanceof Float) {
                Float f1 = Float.valueOf(obj1.toString());
                Float f2 = Float.valueOf(obj2.toString());
                return f1.compareTo(f2) == 0;
            }
            System.out.println("::::::"+obj1.toString()+" <> "+obj2);
            return false;
        } catch (Exception e) {
            System.out.println("=================");
            System.out.println(obj1);
            System.out.println(obj2);
            System.out.println("=================");
            return false;
        }
    }
    public static void outPrint(List logList,List errors,List nullErrors,List differentErrors){
        System.out.println("数据总数:"+logList.size());
        for(Object obj: logList){
            System.out.println(obj);
        }
        System.out.println("错误数:"+errors.size());
        System.out.println(errors);
        System.out.println("无对应数据:"+nullErrors.size());
        System.out.println(nullErrors);
        System.out.println("数据偏差:"+differentErrors.size());
        System.out.println(differentErrors);
    }
}
