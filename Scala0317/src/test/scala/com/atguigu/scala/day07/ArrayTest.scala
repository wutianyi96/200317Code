package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  *    默认创建的集合都是不可变集合！
  *    想创建可变集合，使用 mutable.集合类
  *
  * 不可变数组
  * 创建，添加(:+,+:,++)，
  * 遍历(两种)
  * 变长数组操作： append,remove,update
  * 转换
  * 多维数组  Array.ofDim(x,y)
  * concat,range,fill,foreach
  */

import org.junit._

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class ArrayTest {

  /*
      不可变数组
   */
  @Test
  def test() : Unit ={

    // new
    val a1 = new Array[Int](3)

    val a2 = new Array(3)

    println(a1)
    println(a2)

    // 使用apply
    val a3: Array[Int] = Array(1, 2, 3)


  }

  @Test
  def test2() : Unit ={

    //添加(:+,+:,++)，
    val a3: Array[Int] = Array(1, 2, 3)

    // 向数组的尾部添加一个元素
    val a4: Array[Int] = a3 :+ 4

    println(a3.mkString(","))
    println(a4.mkString(","))

    // 向数组的头部添加一个元素
    val a5: Array[Int] =0 +: a4
    println(a5.mkString(","))

    // ++两个数组进行运算
    val a6: Array[Int] = a4 ++ a3
    println(a6.mkString(","))

    //修改数组中元素的值
    println(a3(0))
    a3(0) = 100
    println(a3(0))

  }

  @Test
  def test3() : Unit ={

    val a3: Array[Int] = Array(1, 2, 3)

    for (elem <- a3) {println(elem)}

    for ( index <- 0 until a3.length) {println(index + "-->" + a3(index))}


  }

  @Test
  def test4() : Unit ={

    //可变
    val buffer: ArrayBuffer[Int] = ArrayBuffer(1, 2, 3)

    //增
    buffer.append(4,5)

    println(buffer.mkString(","))

    //删
    buffer.remove(0)

    println(buffer.mkString(","))

    //改
    buffer.update(0,20)

    println(buffer.mkString(","))

    // 查
    println(buffer(0))



  }

  @Test
  def test5() : Unit ={

    //　不可变转可变

    val array: Array[Int] = Array(1,2,3)

    val buffer: mutable.Buffer[Int] = array.toBuffer

    // 可变转不可变
    val array1: Array[Int] = buffer.toArray



  }

  @Test
  def test6() : Unit ={

    //  有Array中存放了 2个Array[Int]，每个Array[Int]有3个Int类型的数
    //  2 * 3
    val array: Array[Array[Int]] = Array.ofDim[Int](2, 3)

    array(0)(2)=10
    array(0)(1)=9
    array(0)(0)=8

    for(i<-array){

      for(j<-i){
        print(j+"\t")
      }
      println()
    }
  }

  @Test
  def test7() : Unit ={

    //concat,range,fill,foreach
    val array1: Array[Int] = Array(1,2,3)
    val array2: Array[Int] = Array(4,5)

    // 扁平化 ：  将多个集合中的元素取出，返回一个新的集合
    val array3: Array[Int] = Array.concat(array1, array2)

    println(array3.mkString(","))

    // 生成指定范围的数组
    val array: Array[Int] = Array.range(1, 4)

    println(array.mkString(","))

    // 生成存放指定个数元素的数组
    val array4: Array[String] = Array.fill(5)("hello")

    println(array4.mkString(","))

    // 遍历元素，执行指定操作
    array4.foreach( elem => println(elem + "~ 树哥！") )
  }

}
