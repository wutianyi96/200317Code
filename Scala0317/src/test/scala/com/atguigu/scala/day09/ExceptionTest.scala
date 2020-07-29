package com.atguigu.scala.day09

/**
  * Created by VULCAN on 2020/7/8
  *
  *    Java中：
  *              编译时异常：  在编译时，就检测出的异常！
  *                          FileNotFoundException
  *               运行时异常：
  *                          NullPointException
  *                          ArithmeticException	//算法异常
  *
  *              使用try{}
  *              catch(异常1){}
  *              catch(异常2){}
  *              finally{ // 一定会执行 }
  *
  *              要求：  大的异常要放后边！
  *
  *     Scala中：  使用模式匹配处理异常！
  */
import org.junit._
class ExceptionTest {

  @Test
  def test1() : Unit ={

    try {
      println(1 / 0)
    } catch {
      // 大的异常放前面不报错，尽量放后面
      case b: Exception  => println("aaa")
      case a: ArithmeticException  => println(a.getMessage)
      case _ =>

    } finally {
      println("finally")
    }

  }
}
