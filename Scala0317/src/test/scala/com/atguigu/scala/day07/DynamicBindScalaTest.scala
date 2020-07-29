package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  *			动态捆绑
  */
import  org.junit._

class DynamicBindScalaTest {

  @Test
  def test1() : Unit ={

    //优先靠近new的
    val s1:S12 = new S12
    val s2:F12 = new S12

    println(s1.i)
    println(s2.i)

    println(s1.sum())	//220
    println(s2.sum())	//220


  }

}

class F12 {

  val i = 10

  def sum()={
    i + 100
  }
}

class  S12 extends  F12{

  override  val i = 20

  override def sum()={
    i + 200
  }

}
