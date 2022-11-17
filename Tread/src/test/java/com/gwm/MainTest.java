package com.gwm;

import java.util.Scanner;

public class MainTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入密码:");
        String string = sc.nextLine();

        //正则
        String str = "^(?![0-9]+$)(?![a-zA-Z]+$)([0-9a-zA-Z]{6,18}$)";

        if(string.matches(str)){
            System.out.println("密码校验成功。");
        }
        else{
            System.out.println("密码校验失败！");
        }
    }
}
