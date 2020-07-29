package com.atguigu.spark.test.day05

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/15
  *
  *    持久化： 将数据进行持久保存，一般持久化指将数据保存到磁盘！
  *
  */
class PersistenceTest {

  /*
      缓存：  提高对重复对象查询的效率

            查询缓存的过程：  ①到缓存中查询，有就返回，没有就查数据库
                            ②缓存一般都使用内存
                            ③缓存都有回收策略
                            ④缓存有失效的情况 (更新了缓存中的对象)

   */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)


    val rdd2: RDD[Int] = rdd1.map(x => {
      println(x + "   map")
      x
    })

    //将rdd2的运算结果缓存
    //val rdd: rdd2.type = rdd2.cache()
    // 第一个行动算子执行时，将rdd2的结果缓存！
    // 默认将RDD2缓存到内存
    //rdd2.cache()
    // rdd2.persist()  等价  rdd2.cache()

    // 指定缓存的存储级别
    rdd2.persist(StorageLevel.MEMORY_AND_DISK)

    // Job1
    rdd2.collect()

    println("--------------------------------------")

    // Job2
    //  从缓存中取出rdd2,写入文件
    rdd2.saveAsTextFile("output")

    Thread.sleep(100000000)

  }

  val sc = new SparkContext(new SparkConf()

    // 在scala中获取一个类想Class类型   ：  classof[类型]
    .setAppName("My app")
    .setMaster("local[*]")
    .registerKryoClasses(Array(classOf[User4]))
  )

  // 提供初始化方法，完成输出目录的清理
  // 在每个Test方法之前先运行
  @Before
  def init(): Unit ={

    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path("output")

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

  }

  // 每次测试完成后运行
  @After
  def stop(): Unit ={
    sc.stop()
  }

}
