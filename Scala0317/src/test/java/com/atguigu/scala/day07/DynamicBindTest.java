package com.atguigu.scala.day07;

/**
 * Created by VULCAN on 2020/7/6
 *
 *           如果子类和父类有同名的属性，父类的属性称为隐藏属性，访问隐藏属性，需要使用父类的引用！
 *
 *           动态绑定机制：  当将一个子类的实例赋值给父类的引用，调用方法时，JVM会自动将方法和真实指向的对象绑定！
 *                          但是如果方法中有调用属性，根据就近原则！
 */
public class DynamicBindTest {

    public static void main(String[] args) {

        S11 s11 = new S11();

        F11 s12 = new S11();

        System.out.println(((F11)s11).i);  //20
        System.out.println(s12.i);  // 10


        System.out.println(s11.sum());  // 40
        System.out.println(s12.sum());  //  40

        System.out.println(s11.getI());  // 120
        System.out.println(s12.getI());  //  120

        System.out.println(s11.sum1());  // 120
        System.out.println(s12.sum1());  //  120

    }
}

class F11{



    public int sum(){

        return i + 10;
    }

    public int getI(){
        return i + 10;
    }

    public int i = 10;

    public int sum1(){

        return getI() + 20;
    }
}

class S11 extends  F11{

    public int sum1(){

        return getI() + 100;
    }

    public int getI(){
        return i + 100;
    }

    public int i = 20;

    public int sum(){

        return i + 20;
    }







}