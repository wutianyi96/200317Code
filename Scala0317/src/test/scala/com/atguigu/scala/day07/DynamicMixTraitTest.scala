package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *    动态混入：    class 类名 ,在我需要混入的时候，再混入！
  *
  *    静态混入：    class 类名 extends 特质1 with 特质2 ...
  */

import  org.junit._
class DynamicMixTraitTest {

  @Test
  def test() : Unit ={

    val c1 = new C14

    // c1.select()

    // 在某种特定情况下，需要运行select() //C14原本是小白，混入特质后摇身一变
    // 匿名实现
    val c2 = new C14 with MySqlOperate {
      override def insert(): Unit = {
        println("执行insert into")
      }
    }

    c2.select()
    c2.insert()

    val c3 = new C14 with MySqlOperate {
      override def insert(): Unit = {
        println("执行insert overwrite")
      }
    }

    c3.select()
    c3.insert()
  }

  @Test
  def test2() : Unit ={

    //简写  提供了抽象类的一个匿名实现，这个匿名实现类又混入了MySqlOperate
    val c = new C15 with MySqlOperate {
      override def insert(): Unit = {
        println("执行insert C15")
      }
    }

    c.select()
    c.insert()

    //new C15 {}  匿名类
    val c1: C15 = new C15() with MySqlOperate{

      //类名
      override def insert(): Unit = ???
    }
  }

}

trait MySqlOperate{

  def select()={
    println("执行select")
  }

  def insert()


}

//我是小白
class C14 {}

abstract class C15{}
