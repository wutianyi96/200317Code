package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  * 不可变list
  * Nil, :+,+:,list(x), :: , :::
  * 可变list
  * append,list(x),+=,++=
  */
import org.junit._

import scala.collection.mutable.ListBuffer
class ListTest {

  @Test
  def test1() : Unit ={

    val list = List(1, 3, 4)

    println(list)

    // 一个空的集合
    println(Nil)

    val list2: List[Int] = Nil :+ 5
    val list3: List[Int] = 4 +: list2

    println(list2)
    println(list3)

    //读取集合的某个元素
    println(list3(0))

    // :: , :::

    // :: 从右向左运算，将元素加入到集合的头部
    val list1: List[Int] = 3 :: 4 :: 5 :: Nil
    val list5: List[Int] = 3 :: 4 :: 5 :: list

    println(list1)
    println(list5)

    // ::: 要求左右两侧都必须是集合，将两个集合进行扁平化返回
    val list6: List[Int] =3 :: 4 :: list5 ::: Nil
    println(list6)


  }

  @Test
  def test2() : Unit ={

    val buffer: ListBuffer[Int] = ListBuffer(1, 2, 34, 5)

    //append,list(x),+=,++=

    buffer.append(4,5)

    println(buffer)

    // += 只能一个个添加
    buffer += 6

    println(buffer)

    // ++= 添加一个集合

    val array: Array[Int] = Array(1, 2)

    buffer ++= array
    println(buffer)



  }

}
