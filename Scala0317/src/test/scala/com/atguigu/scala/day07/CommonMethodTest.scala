package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  */
import  org.junit._
class CommonMethodTest {

  @Test
  def test() : Unit ={

    val list1 = List(1, 2, 3, 4)
    val list2 = List( 3, 4,5,3,4)

    //length长度
    println(list1.length)
    //size长度
    println(list1.size)
    //foreach遍历
    list1.foreach(x=> println(x))

    //isEmpty是否为空
    println(list1.isEmpty)

    //sum求和
    println(list1.sum)
    //max最大值
    println(list1.max)
    //min最小
    println(list1.min)
    //product乘积
    println(list1.product)

    println("-----------------------------")
    //head头一个
    println(list1.head)
    //tail除了头
    println(list1.tail.tail)
    //last最后一个
    println(list1.last)

    //init除了最后一个
    println(list1.init)

    println("-----------------------------")
    //reverse反转
    println(list1.reverse)
    //toSet去重
    println(list2.toSet) //Set(1, 2, 3, 4)
    //distinct去重
    println(list2.distinct) //List(1, 2, 3, 4)
    //take取前几  topN
    println(list1.take(2))

    //drop从左侧丢弃N个
    println(list1.drop(2))
    println("-----------------------------")
    //dropRight从右侧丢弃N个
    println(list1.dropRight(3))
    // contains是否包含
    println(list1.contains(1))
    //union并集
    println(list1.union(list2))
    //intersect交集
    println(list1.intersect(list2)) //List(3, 4)
    //diff差集
    println(list1.diff(list2)) //List(1, 2)
    println(list2.diff(list1)) //List(5, 3, 4)

  }

}
