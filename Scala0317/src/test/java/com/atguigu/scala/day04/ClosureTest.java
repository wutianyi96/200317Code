package com.atguigu.scala.day04;


/**
 * Created by VULCAN on 2020/7/3
 */
public class ClosureTest {


    /*
            内部类如果要使用外部变量，必须将外部变量声明为final
     */
    public static void main(String[] args) {

         int i=0;

        //启动一个线程
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println( Thread.currentThread().getName()+":  "+i);
            }
        }).start();

        // main
        //i=1;

    }
}
