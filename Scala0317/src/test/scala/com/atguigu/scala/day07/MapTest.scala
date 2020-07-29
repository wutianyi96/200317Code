package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  * 不可变map
  * 读，updated,有序
  * 可变map
  * 使用对偶元组创建，无序，put，update
  * 查询(),contains,get,getOrElse
  *
  * 修改value, +=, getOrElseUpdate,-=
  * 遍历
  * 对偶元组，遍历key，遍历value，遍历tuple
  */

import org.junit._

import scala.collection.mutable

class MapTest {

  @Test
  def test1() : Unit ={

    //创建
    // 不可变Map是有序的
    val map = Map(("k1", 1), ("k2", 2), ("k3", 3))

    val map1 = Map("k1" -> 1, "k22" -> 22,"k2" -> 2, "k3" -> 3)

    println(map)
    println("map1:"+map1)

    //获取某个key对应的Value
    println(map("k1"))


    //不太确定key是否存在
    val value: AnyVal = if (map.contains("k11")) map("k11") else println("无此数据！")

    println(value)

    /*
        Option 代表选项，有Some和None两个实现！
            Some: 存在
            None： 没有
     */
    val maybeInt: Option[Int] = map.get("k11")

    // isDefined: 如果option是Some返回true
    // 如果有值就取出，没有就使用默认值0
    val res = if (maybeInt.isDefined) maybeInt.get else 0

    println(res)

    // scala提供，和上面功能一样
    val i: Int = map.getOrElse("k1", 0)

    println(i)

    //有添加的作用
    val map2: Map[String, Int] = map1.updated("k22", 33)

    println(map2)



  }

  @Test
  def test2() : Unit ={

    var map: mutable.Map[String, Int] = mutable.Map(("k1", 1), ("k2", 2), ("k3", 3))

    //直接添加一个entry
    map += (("k11", 11))

    //修改
    map("k11") = 12

    println(map)

    // 新增或修改
    map.update("k33",33)
    map.update("k11",32)

    // 新增，也可以修改
    map.put("k15",15)
    map.put("k15",17)

    println(map)

    // 如果有就查询出来，没有就添加
    val result: Int = map.getOrElseUpdate("k25", 20)

    println(result)

    println(map)


  }

  @Test
  def test3() : Unit ={

    // 可变map是无序的
    var map: mutable.Map[String, Int] = mutable.Map(("k1", 1), ("k2", 2), ("k3", 3))

    // 将k-v都取出
    for (kv <- map) {
      println(kv)}


    //推荐
    for ((k,v) <- map) {
      println(k+":"+v)}

    for (kv <- map) {
      println(kv._1+"-->"+kv._2)}
    // 取key

    for (key <- map.keys) {
      println(key)  }

    // 取value
    for (v <- map.values) {
      println(v)  }



  }

}
