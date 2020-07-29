package com.atguigu.scala.day04;

import com.atguigu.scala.day06.MyClass;

/**
 * Created by VULCAN on 2020/7/4
 */
public class ModifierTest2 {

    public static void main(String[] args) {

        MyClass myClass = new MyClass();

        System.out.println(myClass.address);

        /*
          不同包，也没有子父关系
        System.out.println(myClass.age);
        System.out.println(myClass.gender);*/

        // name private
     //   System.out.println(myClass.name);

        myClass.printAddress();

         /*
        myClass.printage();
        myClass.printgender();*/
       // private
       // myClass.printName();



    }
}
