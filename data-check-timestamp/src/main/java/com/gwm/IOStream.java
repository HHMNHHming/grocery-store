package com.gwm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IOStream {
    public static void main(String[] args) {
            readRequestId();
    }

    public static void readRequestId()  {
        List<String> logList = new ArrayList();
       try {
           //利用字符流读取文件
           FileReader filereader = new FileReader("E:/Data/data1.log");
           BufferedReader bufferedReader = new BufferedReader(filereader);

           String s;//读取一行的字符串s
           while ((s = bufferedReader.readLine()) != null) {
               System.out.println(s);

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
               System.out.println(json);
               System.out.println("key:" + "requestId" + "的值:" + json.get("requestId"));//得到的是Object
               System.out.println("key:" + "requestId" + "的类型:" + json.get("requestId").getClass().getSimpleName());//得到的是JsonObject
               System.out.println("toString之前");
                logList.add(json.get("requestId").toString());
               System.out.println("在loglist之前");
               System.out.println(logList);
               System.out.println("在loglist之后");
               bufferedReader.close();
           }

       }
        catch (Exception e){
           }
    }

    public static void connectMongodb(){
        Mongo m = new Mongo( "localhost" , 27017 );
        DB db = m.getDB("test" );
        Set<String> colls = db.getCollectionNames();

        for (String s : colls) {
            System.out.println(s);
        }
        DBCollection coll = db.getCollection("student");
        System.out.println(coll);
        System.out.println(coll.getCount());
        BasicDBObject queryObject = new BasicDBObject("name","muguilin");
        DBObject obj = coll.findOne(queryObject);
        System.out.println(obj);
        JSONObject jsonObject = JSON.parseObject(obj.toString());
        System.out.println(jsonObject.get("name").toString());
    }

}
