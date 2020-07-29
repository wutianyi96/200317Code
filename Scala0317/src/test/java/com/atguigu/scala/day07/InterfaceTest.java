package com.atguigu.scala.day07;

/**
 * Created by VULCAN on 2020/7/6
 */
public class InterfaceTest {

    public static void main(String[] args) {

        F1 f1 = new F1();

        // 直接实现的接口
        Class<?>[] interfaces = f1.getClass().getInterfaces();

        //B1
        for (Class<?> anInterface : interfaces) {

            System.out.println(anInterface.getSimpleName());

        }

        E1 f2 = new F1();
        B1 f3 = new F1();

        A1 f4 = new F1();

        C1 f5 = new F1();




    }

}

interface A1 extends  C1{}
interface B1{}
interface C1{}

interface D1 extends A1,B1,C1{}

class E1 implements A1{}
class F1 extends E1 implements B1{}
