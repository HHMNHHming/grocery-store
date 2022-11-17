package com.gwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class compareJson {
    public static void main(String[] args) {
        //readFromLog();
        //connectMongodb(readFromLog());
        List<JSONObject> logList = readFromLog();
        List<JSONObject> mongoList = connectMongodb(logList);
        compare(logList, mongoList);
    }

    /*
     *参数
     *返回值 logList<JSONObject>
     **/
    public static List<JSONObject> readFromLog() {
        try {
            //利用字符流读取文件
            FileReader filereader = new FileReader("E:/Data/upload_data0816-1543-1613.log");
            BufferedReader bufferedReader = new BufferedReader(filereader);

            List<JSONObject> logList = new ArrayList<>();

            String s;//读取一行的字符串s
            while ((s = bufferedReader.readLine()) != null) {
                System.out.println("log文件中的数据:" + s);

                //如何转化成json字符串呢？
                //1 使用序列化的方式转换成json字符串，然后获取requestId的值
/*            JSONObject object = JSONObject.parseObject(s);
            String pretty = object.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);

            System.out.println(pretty);//这里获取的是json字符串
            System.out.println(pretty.getClass().getSimpleName());
            JSONObject jsonObject = JSONObject.parseObject((pretty));
            System.out.println(jsonObject.get("requestId"));
            System.out.println(jsonObject.get("requestId").getClass().getSimpleName());*/


                //2 使用阿里的转换器,先转换成json对象，然后获取requestId的值
                JSONObject json = JSONObject.parseObject(s);
                System.out.println("转换成json数据:" + json);
                System.out.println("key:" + "timestamp" + "的值:" + json.getJSONObject("DevInfo").get("timestamp"));//得到的是Object 然后使用toString转换成String
                //System.out.println("key:" + "timestamp" + "的类型:" + json.get("timestamp").getClass().getSimpleName());//得到的是JsonObject
                logList.add(json);
            }
            bufferedReader.close();
            System.out.println("在循环以后的logList:" + logList);
            return logList;
        } catch (Exception e) {
            System.out.println("获取数据异常···");
            return null;
        }
    }

    /*
     * 参数 logList<JSONObject>
     * 返回值 mongoList<JSONObject>
     * */
    public static List<JSONObject> connectMongodb(List<JSONObject> logList) {
        MongoClientURI uri = new MongoClientURI("mongodb://root:123456@39.107.136.198:11101/");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase("mdc-data");
        System.out.println("连接数据库成功···");

        MongoCollection<Document> colls = db.getCollection("2022-08-16");
/*        FindIterable<Document> documents = colls.find();
        System.out.println("2022-08-15 documents" + documents);
        MongoCursor<Document> mongoCursor = documents.iterator();
        System.out.println("获取游标成功，mongoCursor：" + mongoCursor);*/
        List<JSONObject> mongoList = new ArrayList<>();

/*        //通过迭代的方式查找数据并找出requestId，大量数据的时候并不适合这么处理 遗弃了
        while (mongoCursor.hasNext()) {
            //System.out.println(mongoCursor.next());
            String string1 = mongoCursor.next().toJson();//先转换成json字符串
            JSONObject jsonObject = JSON.parseObject(string1);//转成json对象
            //System.out.println("key:" + "requestId" + "的值:" + jsonObject.get("requestId").toString());//requestId的String字符串
            mongoList.add(jsonObject);
        }*/

        //通过过滤器去数据库查找，速度比迭代应该快一点···
        for (int i = 0; i < logList.size(); i++) {
            FindIterable<Document> iter = colls.find(new Document("grabTimestamp", logList.get(i).getJSONObject("DevInfo").get("timestamp")));
            System.out.println("迭代对象:"+iter);
            MongoCursor<Document> mongoCursor = iter.iterator();
            while (mongoCursor.hasNext()) {
                //System.out.println(mongoCursor.next());
                String string1 = com.mongodb.util.JSON.serialize(mongoCursor.next());//先转换成json字符串
                System.out.println("一条游标mongo数据:"+string1);
                JSONObject jsonObject = JSON.parseObject(string1);//转成json对象
                //System.out.println("key:" + "requestId" + "的值:" + jsonObject.get("requestId").toString());//requestId的String字符串
                mongoList.add(jsonObject);
            }

        }

        System.out.println("检索所有文档完成");
        System.out.println("循环以后的mongoList:" + mongoList);
        System.out.println("第一个mongolist的content:" + mongoList.get(0).getJSONObject("content"));
        return mongoList;
    }

    public static void compare(List<JSONObject> logList, List<JSONObject> mongoList) {
        List<JSONObject> contentList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        System.out.println("loglist大小:"+logList.size());
        System.out.println("mongoList大小：:"+mongoList.size());
        if (logList.size() == mongoList.size()) {
            for (int i = 0; i < mongoList.size(); i++) {
                contentList.add(mongoList.get(i).getJSONObject("content"));
            }
            System.out.println(contentList);
            for (int j = 0; j < logList.size(); j++) {
                System.out.println("车端:" + logList.get(j) + "\n" + "云端:" + contentList.get(j));
                JSONObject source = contentList.get(j);
                JSONObject target = logList.get(j);
                if (!check(source, target)) {
                    System.out.println(logList.get(j).getJSONObject("DevInfo").get("timestamp") + "的数据不相同");
                    list.add(logList.get(j).getJSONObject("DevInfo").get("timestamp").toString());
                }
                else{
                    System.out.println("数据一致···");
                }
            }
        }
        else{
            System.out.println("车端数据与数据库数据不完全一致，请检查车端数据···");
        }
        System.out.println("数据不一致的timestamp为:" + list);
    }

    public static boolean check(Object obj1, Object obj2) {

        try {

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
            e.printStackTrace();
            return false;
        }
    }
}