package com.atguigu.scala.day10;

import java.util.ArrayList;

/**
 * Created by VULCAN on 2020/7/10
 */
public class GenericTest {

    public static void main(String[] args) {

        //向集合中添加元素
        ArrayList list = new ArrayList();

        // list.add(Object)  可以放入任意类型，对类型没有校验，不安全

        list.add(1);
        list.add("String");

        // 使用时， 强转为指定的类型
        Object o = list.get(1);

        ArrayList<Integer> list1 = new ArrayList<>();

        // 在向集合中加入元素时，进行类型检查
        list1.add(1);

        // 省略了强制转换
        Integer integer = list1.get(0);

    }
}
