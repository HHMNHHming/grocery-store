package com.gwm;

import java.util.Scanner;

public class Test {

    @org.junit.Test
    public  void test(){
         Scanner sc = new Scanner(System.in);
         String string = "adf34sdafg";

         //正则
        String str = "^(?![\\d]+$)(?![a-zA-Z]+$)([0-9a-zA-Z]{6,18}$)";

        if(string.matches(str)){
            System.out.println("密码校验成功.");
        }
        else{
            System.out.println("密码校验失败！");
        }

    }
 }
