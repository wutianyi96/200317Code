package com.atguigu.scala.day09

/**
  * Created by VULCAN on 2020/7/8
  *
  *   隐式值：    当一个变量使用implicit修饰，此时变量赋值后，变量称为隐式变量
  *              变量引用的值称为隐式值！
  *
  *              不能有二义性！
  *              匹配时，只参考声明的唯一类型！
  *
  *   隐式参数：  当一个函数的参数列表中，有implicit修饰的参数时，这种参数称为隐式参数
  *
  *              方法名 ：  使用隐式值
  *              方法名():  使用默认值
  *              方法名("x"): 使用传入的参数
  *
  *              优先级：  传入的参数  >  隐式值/默认值
  *
  *
  */

import  org.junit._
class ImplicitValue {

  @Test
  def test() : Unit ={

    // s：隐式变量 "jack"是隐式值
    implicit  val s1:String ="jack"

    //implicit  val s2:String ="tom"


    // name就是一个隐式参数
    def hello(implicit name : String="marry") ={

      println("hello：" + name)

    }

    //调用方法时，可以不传入隐式参数的值，此时由编译器自动赋值
    // 编译器自动搜索方法范围内，所有的隐式值，找到合适的类型就自动传值
    hello
    hello()
    hello("tony")

  }

  @Test
  def test2() : Unit ={

    //召唤隐式值
    val ord: Ordering[Int] = implicitly[Ordering[Int]]

    val list = List(11, 2, 3, 4)

    // 自己传入当前OrdingInt类型的隐式值
    //List(2, 3, 4, 11)
    println(list.sortBy(x => x)(ord))

  }

}
