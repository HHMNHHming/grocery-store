package com.gwm;


public class StringTest {
    @org.junit.Test
    public void test(){
        String str = "abc";
        String str2 = new String("abc");
        String str3 = new String("abc");
        boolean bool = str==str2;
        System.out.println("地址相同吗?"+bool);
        System.out.println("值相同吗?"+str.equals(str2));

        System.out.print("str2和str3地址相同吗？");
        System.out.println(str2==str3);
        System.out.print("str2和str3值相同吗？");
        System.out.println(str2.equals(str3));


        //不适用new关键字，两个字符串都存于栈中，str5和str6的地址一样;
        String str5 = "ming";
        String str6 = "ming";
        System.out.println(str5==str6);
        System.out.println("str5和str6的值相同吗？"+str5.equals(str6));

    }
}
