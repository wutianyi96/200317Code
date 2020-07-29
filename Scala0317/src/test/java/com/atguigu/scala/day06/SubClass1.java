package com.atguigu.scala.day06;

/**
 * Created by VULCAN on 2020/7/4
 */
public class SubClass1 extends MyClass {

    // 同包
    public static void main(String[] args) {

        SubClass1 subClass1 = new SubClass1();

        System.out.println(subClass1.gender);

        subClass1.printgender();

    }
}
