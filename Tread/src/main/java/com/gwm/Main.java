package com.gwm;

public class Main {
    public static void main(String[] args) {
        //创建线程的对象
        MyThread t1 = new MyThread();
        MyThread t2 = new MyThread("线程2");


        t1.start();
        t2.start();

        t1.setName("线程1");

    }
}
