package com.atguigu.scala.day06;

import com.sun.deploy.security.MozillaMyKeyStore;

/**
 * Created by VULCAN on 2020/7/4
 */
public class ModifierTest1 {

    public static void main(String[] args) {

        MyClass myClass = new MyClass();

        System.out.println(myClass.address);

        System.out.println(myClass.age);
        System.out.println(myClass.gender);

        // name private
     //   System.out.println(myClass.name);

        myClass.printAddress();
        myClass.printage();
        myClass.printgender();
       // private
       // myClass.printName();



    }
}
