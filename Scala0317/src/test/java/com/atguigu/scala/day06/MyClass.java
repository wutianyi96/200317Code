package com.atguigu.scala.day06;

import sun.reflect.generics.repository.GenericDeclRepository;

/**
 * Created by VULCAN on 2020/7/4
 */
public class MyClass {

    private String name="张三";

    int age = 20;

    protected String gender="男";

    public String address="sz";

    private void printName(){

        System.out.println(name);

    }

     void printage(){

        System.out.println(age);

    }

    protected void printgender(){

        System.out.println(gender);

    }

    public void printAddress(){

        System.out.println(address);

    }
}
