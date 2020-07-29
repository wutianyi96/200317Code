package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *    7.Queue  队列，先进先出
  * +=,++=,enquene,dequene
  */
import org.junit._

import scala.collection.mutable
class QueneTest {

  @Test
  def test1() : Unit ={

    val queue: mutable.Queue[Int] = mutable.Queue(1, 2, 3, 4)

    queue += 5

    println(queue)

    val array: Array[Int] = Array(6, 7, 8)

    queue ++= array

    println(queue)

    queue.enqueue(9,10)

    println(queue)

    //队列，先进先出
    queue.dequeue()

    println(queue)

    /*
    Queue(1, 2, 3, 4, 5)
    Queue(1, 2, 3, 4, 5, 6, 7, 8)
    Queue(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    Queue(2, 3, 4, 5, 6, 7, 8, 9, 10)
    */

  }

}
