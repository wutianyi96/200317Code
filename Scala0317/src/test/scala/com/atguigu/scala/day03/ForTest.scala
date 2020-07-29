package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 * 在java中有：
 * for(int i = 0 ; i<x ; 自定义步长 ){
 * 循环体
 * }
 *
 * 增强For： 通常是遍历集合
 * for ( X x:  集合){
 *              x.xxxx
 * }
 *
 * 在scala中，只有一种for, 既可以按照指定的步长进行循环，还可以迭代集合！
 */

import org.junit._

import scala.util.control.Breaks._



import scala.util.control.Breaks

class ForTest {

  @Test
  def test1(): Unit = {

    /*
        Range(1,5): 起始是调用了Range的apply(1,5)
                在scala中，很多类都提供了apply(),作用就是快速构建一个对象！

              def apply(start: Int, end: Int): Range = new Range(start, end, 1)

              Range(1,5) 等价于  new Range(1, 5, 1)

              Make a range from `start` until `end` (exclusive) with step value 1.

        [1,5)
     */
    for (i <- Range(1, 5)) {
      println(i)
    }

    println("-----------------------------------------------")


    /*
        Int 类型的功能和Java中的Int类型功能是一样！

         scala作者发明了加强的Int----> RichInt，当调用一些特殊方法时，会将Int 隐式转换为RichInt
              String ----->  StringOps

          [1,5)
     */
    /*for( i  <-  1.until(5)){
      println(i)
    }*/

    //简化
    for (i <- 1 until 11) {
      println(i)
    }


    println("-----------------------------------------------")

    /*
        [1,5]
     */
    for (i <- 1.to(10)) {
      println(i)
    }


    println("-----------------------------------------------")

    /*
         类( 参数 )： 调用类.apply() 返回一个对象

         增强for
     */
    val ints = List(1, 2, 3, 4, 5)

    for (i <- ints) {
      println(i)
    }


  }

  // 调整步长
  @Test
  def test2(): Unit = {

    for (i <- Range(1, 5, 2)) {
      println(i)
    }

    println("-----------------------------------------------")

    // by: 本质是一个方法，用来设置步长
    for (i <- 1 until 11 by 3) {
      println(i)
    }

    println("-----------------------------------------------")

    // 逆序
    for (i <- 11 until 1 by -3) {
      println(i)
    }

  }

  /*
      循环守卫(门卫，保安)： for循环中如果有判断，或其他条件表达式，进行简化书写方式
   */
  @Test
  def test3(): Unit = {

    for (i <- 1 to 10) {
      //只输出奇数
      if (i % 2 == 1) {
        println(i)
      }

    }

    println("--------------简化后--------")

    //只输出奇数
    for (i <- 1 to 10 if (i % 2 == 1)) println(i)


    println("--------------再举例--------")

    for (i <- 1 to 10; j <- 2 to 5 if (i == j)) println(i)

    println("--------------等价于--------")


    //如果以下两个位置有逻辑，不能简写
    for (i <- 1 to 10) {
      // 逻辑
      for (j <- 2 to 5) {
        if (i == j) {
          println(i)
        }
      }
      // 逻辑
    }

  }

  /*
      循环中引入变量
   */
  @Test
  def test5(): Unit = {

    for (i <- 1 to 10) {
      var j = i * 2
      println(j)
    }

    println("--------------简化后--------")

    for {i <- 1 to 10
         j = i * 2} println(j)

  }

  /**
   for循环返回值(for 推导式)

   yield 用来把一个集合中的元素，通过计算后，放入另一个集合！
   */
  @Test
  def test6(): Unit = {

    /*val res= for (i <- 1 to 5 ) {
      i
    }
    println(res)*/
    // ()

    val res1 = for (i <- 1 to 5 if (i % 2 == 1)) yield {
      i * 2
    }

    println(res1)
  }


  //打印1~100之间所有是9的倍数的整数的个数及总和
  @Test
  def test7(): Unit = {

    var count = 0
    var sum = 0

    for (i <- 1 to 100 if (i % 9 == 0)) {
      count += 1
      sum += i
    }

    printf("打印1~100之间所有是9的倍数的整数的个数: %d 及总和: %d ", count, sum)


  }

  /*
输出以下表达式
0+6=6
1+5=6
...
6+0=6


 */

  @Test
  def test8(): Unit = {

    for (i <- 0 to 6) printf(" %d + %d = 6 \n", i, 6 - i)

  }

  /*
      打印99乘法表
      1 * 1 = 1
      1 * 2 = 2     2 * 2 = 4
        ...

      1 * 9 = 9      2 * 9 =18 .....         9 * 9 = 81

      最外层循环负责打印每一行   i:1-9
          里层循环负责打印每一行的每一列  j:1-i
             1 * 1 = 1 :   j * i = j * i


   */
  @Test
  def test9(): Unit = {

    for (i <- 1 to 9) {

      for (j <- 1 to i) {
        printf("%d * %d = %d\t", j, i, j * i)
      }
      println()
    }

  }

  /**
     在scala中，没有break和contine，应该通过函数来实现这两个关键字的功能！

      def break(): Nothing = { throw breakException }
   */

  // continue的实现
  @Test
  def test10(): Unit = {

    for (i <- 1 to 9) {

      println("当前的参数是:" + i)

      if (i % 2 == 1) {
        //continue 跳出本次循环，开始下次循环
      }
      println(i)
    }


    // 使用循环守卫可以实现
    for (i <- 1 to 9 if (i % 2 == 0)) println(i)

    println("--------------复杂continue的实现-------------")

    for (i <- 1 to 9) {

      try {
        println("当前的参数是:" + i)

        if (i % 2 == 1) {
          throw new Exception("想跳过continue")
        }

        println(i)

      } catch {
        case _ =>
      }
    }


  }

  @Test
  def test11(): Unit = {

    println("--------------复杂continuescala作者的实现-------------")

    for (i <- 1 to 9) {

      /*
        breakable(op: => Unit) : 接收的是一个传入没有类型的，返回Unit类型的参数  {xxxx ; xx ;xxx;xxx}
       */
      Breaks.breakable {
        println("当前的参数是:" + i)

        if (i % 2 == 1) {
          // 抛出一个BreakControl类型的异常
          Breaks.break()
        }

        println(i)
      }


    }
  }

  @Test
  def test12(): Unit = {

    println("--------------复杂break scala作者的实现-------------")

      /*
        breakable(op: => Unit) : 接收的是一个传入没有类型的，返回Unit类型的参数  {xxxx ; xx ;xxx;xxx}
       */
      Breaks.breakable {
        for (i <- 1 to 9) {
        println("当前的参数是:" + i)

        if (i % 2 == 1) {
          // 抛出一个BreakControl类型的异常
          Breaks.break()
        }

        println(i)
      }
    }

    // 后续逻辑
    println("已经跳出循环")
  }


  @Test
  def test13(): Unit = {

    println("--------------简化break scala作者的实现-------------")

    /**
      breakable(op: => Unit) : 接收的是一个传入没有类型的，返回Unit类型的参数  {xxxx ; xx ;xxx;xxx}
     */

   breakable {
      for (i <- 1 to 9) {
        println("当前的参数是:" + i)

        if (i % 2 == 1) {
          // 抛出一个BreakControl类型的异常
          break
        }

        println(i)
      }
    }

    // 后续逻辑
    println("已经跳出循环")
  }


}
