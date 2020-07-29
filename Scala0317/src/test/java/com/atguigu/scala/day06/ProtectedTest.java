package com.atguigu.scala.day06;

/**
 * Created by VULCAN on 2020/7/4
 */
public class ProtectedTest {

    public static void main(String[] args) throws CloneNotSupportedException {

        // 任意类都是Object的子类，在Object 有clone
        //  protected native Object clone()

        Son son = new Son();

        // clone()' has protected access in 'java.lang.Object'
        //  ProtectedTest.main() 在运行时，希望访问 son中的clone()

        //  ProtectedTest和 Object不是同包的！，因此无法访问
        //son.clone();

        ProtectedTest protectedTest = new ProtectedTest();

        protectedTest.clone();

        Object o = new Object();

        //'clone()' has protected access in 'java.lang.Object'
       // o.clone();

    }

}



class Son{

    public  void sayHello() throws CloneNotSupportedException {

        clone();

    }

  /*  @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }*/
}
