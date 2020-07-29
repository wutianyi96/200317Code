package com.atguigu.scala.day04;

import com.atguigu.scala.day06.MyClass;

/**
 * Created by VULCAN on 2020/7/4
 */
public class SubClass2 extends MyClass {

    public static void main(String[] args) {

        // 子类调用了继承自父类中的方法
        SubClass2 subClass1 = new SubClass2();

        System.out.println(subClass1.gender);

        subClass1.printgender();

    }
}
