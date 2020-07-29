package com.atguigu.scala.day07;

import java.util.ArrayList;

/**
 * Created by VULCAN on 2020/7/6
 */
public class MutableTest {

    /*
           不可变集合： 当只对一个集合有只读操作！
                         不可变集合在多线程环境下，效率高！

             可变集合：          频繁的写的需求

     */
    public static void main(String[] args) {

        //数组
        int [] nums = new int[]{1,2,3};

        // 数组是不可变集合，数据一旦初始化，数组的大小和类型是确定！无法扩容！
        // 数组中的内容可以变化

        nums[0] = 4;

        // Arraylist是可变集合，动态扩容。 一旦扩容，在底层会重新创建一个新的集合，将之前集合的内容进行复制
        // 再赋值给变量

        ArrayList<Integer> list = new ArrayList<>();

        System.out.println(list.hashCode());

        list.add(1);
        list.add(1);
        list.add(1);

        System.out.println(list.hashCode());

    }

}
