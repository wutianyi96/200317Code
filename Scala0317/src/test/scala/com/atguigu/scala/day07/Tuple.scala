package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  * 创建，取值，按照索引取值productElement(x),获取迭代器遍历
  * 对偶元组
  *
  * Tuple: 将任意类型的数据，放入到一个集合！
  *          tuple最多支持放入22个元素！
  */

import  org.junit._
class Tuple {

  @Test
  def test1() : Unit ={

    //创建
    val tuple: (Int, Int, Double, String) = (1, 2, 20.3, "hello")

    //取第一个
    println(tuple._1)
    //取第四个
    println(tuple._4)

    //使用索引取
    println(tuple.productElement(0))

    //遍历
    val iterator: Iterator[Any] = tuple.productIterator

    for (elem <- iterator) { println(elem) }

  }

  @Test
  def test2() : Unit ={

    // 如果tuple固定有2个元素，称为对偶元组  类型Tuple2类型  Map结构，每个Entry，就是一个Tuple2(对偶元组)
    val tuple: (Int, Int) = (1, 2)

    val tuple1: (Int, Int) = 3 -> 4

  }

}
