package com.atguigu.scala.day02

/**
 * Created by VULCAN on 2020/6/29
 */

import  org.junit._
class TypeTest {

  /*
      scala是完全面向对象的语言，一切皆对象
   */
  @Test
  def test() : Unit ={

    //1true
    println(1.toString() + true.toString())

    //如果调用一个空参的方法，()可以省略
    println(1.toString + true.toString)

    // 在某些情况下，调用方法时，可以省略.
    println(1 toString  )

  }

  @Test
  def test2() : Unit ={

     val f1 = 1.12345678f

     val d1 =1.12345678

    println(f1)
    println(d1)
  }
  
  @Test
  def test3() : Unit ={

   // var f1 : Float = 1.1   // 1.1 是 Double，不能隐式转换为Float
    var f2 = 1.2 // 1.2推导为Double
    var f3 : Double = 1.3
    var f5 : Double = 1.5f
  }


  /**
      'a' : 是一个常量
      10 ： 也是一个常量

      常量之间的运算，会在编译之前就运算，算完还是常量
          常量运算后赋值给变量，只看范围！
   */
  @Test
  def test4() : Unit ={

    var c1 : Char = 'a' + 10

    println(c1)  //k
  }

  /*
      c1 是变量，变量 和常量进行运算，必须编译后才可以运行！
        变量 和 变量
          或
          变量 和 常量 运算后赋值给变量，既要看类型还要看范围！
    */
  @Test
  def test5() : Unit ={

    var c1 : Char = 'a'

    //c1 = c1 + 10   not ok

    println(c1)
  }
  
  @Test
  def test6() : Unit ={

    var c2: Char = 97 + 10
    var c3: Char = 107
    var c4:Char = 9999
  }
  
  @Test
  def test7() : Unit ={

    var b1:Byte=10
    // 没有隐式转换关系，无法将 Byte 转 Char
   // var c1:Char=b1
  }

  @Test
  def test8() : Unit ={

    var b1:Byte=10
    var c1:Char='a'
    //var s1:Short=b1 +c1// Byte,Char,Short 变量运算都会转为Int
    var s2:Short=100
  }

}
