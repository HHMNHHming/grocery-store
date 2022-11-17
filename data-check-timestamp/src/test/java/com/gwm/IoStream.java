package com.gwm;

import org.junit.jupiter.api.Test;

import java.io.*;

public class IoStream {
    @Test
    public void testIOSteam() throws Exception{
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\IO流\\file1.txt"));
        FileOutputStream fileOutputStream = new FileOutputStream("D:/IO流/file2.txt");

        //字节流InputStream读入，每次read()都要访问硬盘
/*        int i;
        while((i=fileInputStream.read()) !=-1){
            fileOutputStream.write(i);
        }*/

        //字节流InputStream读入法，使用一个数组
/*        int length = fileInputStream.available();
        byte[] b = new byte[length];
        fileInputStream.read(b);
        fileOutputStream.write(b);*/

        //上面这种方式占用的是数组内存 我们需要声明一个晓一点的数组来进行读写
/*        byte[] b = new byte[2048];
        int length;
        while((length = fileInputStream.read(b))!=-1){
            fileOutputStream.write(b,0,length);
        }*/

        //通过java特有的包装类，buffered进行读写操作 他的底层还是用数组的形式实现的  read（）返回值是int void是write（）的返回值
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        int  b ;
        while((b = bufferedInputStream.read())!=-1){
            bufferedOutputStream.write(b);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

}
