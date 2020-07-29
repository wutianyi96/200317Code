package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  * 不可变set
  * add,+=,-=
  * 可变set
  * remove,update()
  */
import org.junit._

import scala.collection.mutable
class SetTest {

  @Test
  def test1() : Unit ={

    var set = Set(1, 2, 3, 3, 4)

    //无序
    println(set)

    set += 5

    println(set)

    set -= 3

    println(set)

  }

  @Test
  def test2() : Unit ={

    val set: mutable.Set[Int] = mutable.Set(1, 3, 4, 5, 4)

    //添加
    set.add(6)

    //删除
    set.remove(1);

    // 删除10，不存在也不报错！
    set.update(10,false)
    // 添加11
    set.update(11,true)

    println(set)

  }

}
