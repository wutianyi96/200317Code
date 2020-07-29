package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *      Trait(特质/特性) ：  类比为Java中的interface + 抽象类 的混合体
  *
  *          trait和java中的interface 可以实现多个！
  *          trait更像抽象类，可以定义普通变量，普通方法！ 不能定义有参的主构造器！
  *
  *          目的： 将多个类共性的特性抽象出来，放在一个专门的类(特性类/特质类)中
  *
  *
  *          声明：  trait 类名 {  }
  *
  *          混入(实现)特性(接口)：    class 类名 extends 特质1  with 特质2 with 特质3 ...
  *                                   class 类名 extends 特质1
  *                                   class 类名 extends 类 with 特质2 with 特质3 ...
  *
  *           scala兼容java的api，java中声明的接口，可以直接作为trait在scala中使用！
  *
  *           trait不能实例化！
  *
  *
  *
  *      java中interface存在的意义：   java也是单继承，interface主要的意义就是扩展单继承！
  *                                    类可以继承一个类，实现多个接口！
  *
  *                                  java中接口是可以多继承！
  *                                  接口和类不是一个体系！
  *
  *                                  接口：  属性都是常量
  *                                          方法都是抽象方法！
  *
  *
  *
  */

import  org.junit._
class TraitTest {

  @Test
  def test() : Unit ={

    val c = new C11

    c.hello()
    c.hi()
    println(c.name)
    println(c.age)



  }
}

trait t1{

  //普通属性
  val name : String ="t1"

  //抽象属性
  val age : Int

  //抽象方法
  def hello()

  //普通方法
  def hi()={
    println("hi")
  }

}

class C11 extends Cloneable with Serializable with t1{
  override val age: Int = 20

  override def hello(): Unit = {
    println("hello")
  }
}




