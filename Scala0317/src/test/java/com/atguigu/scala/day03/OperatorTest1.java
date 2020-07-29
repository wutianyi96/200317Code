package com.atguigu.scala.day03;

/**
 * Created by VULCAN on 2020/6/30
 */
public class OperatorTest1 {

    public static void main(String[] args) {

        /*int i = 0;
        int j = 0;
        *//*
            窍门：  后++，先赋值，再+1

            i++ 并不是一个原子操作

              //i++
            int temp = i
            i=i+1
            //j=i++;
            j=temp

            在多线程环境下，会出现线程安全问题！
                  用AtomicInteger

             单线程下，还很容易引起歧义


         *//*
        j=i++;
        // i = 1, j = 0
        System.out.println("i:"+i+"  j:"+j);*/

       // System.out.println(jiecheng(5));

        int a = 10;
        int b = a = 20 ;
        System.out.println(b);

    }

    /*
       阶乘运算：  5!=  5 * 4! = 5 * 4 * 3 ! =..
            1的阶乘还是1

     */
    public static  int jiecheng(int num){

        if (num <=1){
            return 1;
        }else{
            return  num * jiecheng(num-1);
        }

    }
}
