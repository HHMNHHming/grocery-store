package com.gwm;

public class MyThread extends Thread{
    public MyThread (){
    }

    public MyThread(String str) {
        super(str);
    }

    public void run(){
        for(int i=0;i<100;i++) {
            System.out.println("执行"+this.getName()+"的run方法中···");
        }
    }

}
