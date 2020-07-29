package com.atguigu.scala.day10;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VULCAN on 2020/7/10
 */
public class GenericTest3 {

    public static void main(String[] args) {

        ArrayList<GrandPa> list1 = new ArrayList<GrandPa>();
        ArrayList<Father> list2 = new ArrayList<Father>();
        ArrayList<Son> list3 = new ArrayList<Son>();
        ArrayList<Other> list4 = new ArrayList<Other>();

        GenericTest2 genericTest2 = new GenericTest2();

        // 测试上限
        //genericTest2.test1(list1);  not ok
        genericTest2.test1(list2);
        genericTest2.test1(list3);
        // genericTest2.test1(list4);  // 和Father没有继承关系 not ok

        //测试下限
        genericTest2.test2(list1);
        genericTest2.test2(list2);
        // genericTest2.test2(list3);  not ok
        // genericTest2.test2(list4);  not ok



    }
}

class GrandPa{}
class Father extends  GrandPa{}
class Son extends  Father{}

class Other{}

class GenericTest2{

    // 传入的必须是Father类型  泛型的上限
    public void test1(List<? extends Father > list){

    }

    // 传入的必须是Father继承的类型  泛型的下限 至少需要一个Father
    public void test2(List<? super Father > list){

    }

}
