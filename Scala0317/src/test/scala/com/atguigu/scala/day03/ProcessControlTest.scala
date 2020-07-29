package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 *    流程控制
 */
import  org.junit._
class ProcessControlTest {

  //定义两个变量Int，判断二者的和，是否既能被3又能被5整除，打印提示信息
  //

 @Test
 def test1() : Unit ={

    val i = 25
    val j = 20

    if ( (i + j) % 3 ==0 && (i + j) % 5 ==0 ){
      println("复合条件！")
    }else{
      println("不复合条件！")
    }

 }

  //判断一个年份是否是闰年，闰年的条件是符合下面二者之一：
  //(1)年份能被4整除，但不能被100整除；
  //(2)能被400整除
  @Test
  def test2() : Unit ={

    val year = 2021

    if ( (year % 400 == 0) || (year % 4 == 0 && year % 100 !=0  )){
      println(year+"是闰年！")
    }else{
      println(year+"不是闰年！")
    }
  }

  @Test
  def test3() : Unit ={

    var a=10
    var b = { a=20 }
    println(b)  // ()

  }

  // 4.三元运算符      ( boolean ) ? true : false
  //  在scala中没有三元运算符，使用if-else模拟   如果代码块中只有一行代码，可以省略{}
  @Test
  def test4() : Unit ={

    val i = 10
    val res=if (i > 0) 1 else 2

    println(res)

  }



}
