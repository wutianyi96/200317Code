package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 *    AnyVal类型之间的转换
 *        ①如果有隐式转换，低精度类型可以自动转为高精度类型
 *        ②将高精度类型转低精度
 *            有可能损失精度
 *
 *            java中有 (类型) 强制转换,但是scala认为不复合面向对象特征！
 *            Scala中都使用 toXxx() 进行转换！
 *
 *    String 和 AnyVal类型的转换
 *
 *    AnyRef 和 AnyRef 转换：  看有没有继承关系
 *
 */

import org.junit._
class TypeTransferTest {

  @Test
  def test1() : Unit ={

      var i = 10
      val j = 10.5

        i = j.toInt
    println(i)
  }

  //String 和 AnyVal类型的转换
  @Test
  def test2() : Unit ={

      //AnyVal 转 String  ： 拼接字符串
    val i = 0
    println(i + "")

      //  String 转 AnyVal ,必须是对应类型的值才可以转
    val s = "111"
    println(s.toInt)



  }

}
