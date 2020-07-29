package com.atguigu.scala.day10;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULCAN on 2020/7/10
 *
 *          注意： ①泛型的不可变性
 *
 *
 *           List<Object> list  =   List<String> list2  // 无法成功
 *
 *                List<Object>
 *           类型：  List
 *           泛型：   Object
 *
 *           List<String>
 *               类型：  List
 *                泛型：   Object
 *
 *                类型之间，如果存在子父关系，可以使用多态，将子类类型赋值给父类
 *
 *                泛型其实指类型中某个参数的类型！
 *
 *           List<Object> list  =   ArrayList<Object> list2   // ok
 *
 *           遵守泛型的不可变性！
 *
 */
public class GenericTest1 {

    public static void main(String[] args) {

        ArrayList<Object> list1 = new ArrayList<Object>();

        //  泛型的不可变性
        // ArrayList<Object> list2 = new ArrayList<String>();

        List<String> list2 = new ArrayList<String>();

        List<Object> list3 = new ArrayList<Object>();

        test1(list1);

        // 泛型的不可变性
        // test1(list2);
        test1(list3);

        // test2(list1);
        test2(list2);

        /*
              List<Object> list  =   List<String> list2  // 无法成功
         */
        //test1(list2);

    }


    public static void test1(List<Object> list)  {

    }

    public static void test2(List<String> list)  {

    }
}
