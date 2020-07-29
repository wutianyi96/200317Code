package com.atguigu.spark.test.day06

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
        checkpoint :  将部分阶段运行的结果，存储在持久化的磁盘中！

                  作用： 将RDD保存到指定目录的文件中！
                         切断当前RDD和父RDD的血缘关系
                         在Job执行完成后才开始运行！
                         强烈建议将checkpoint的RDD先缓存，不会造成重复计算！


                     checkpoint也会提交一个Job，执行持久化！
                     在第一个行动算子执行完成后，提交Job，运行！
   */
  @Test
  def test3() : Unit ={

    val list = List(1, 2, 3, 4)

    //指定checkpiont的目录
    sc.setCheckpointDir("checkpoint")

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)


     val rdd2: RDD[Int] = rdd1.map(x => {
       println(x + "   map")
       x
     })

     val rdd3: RDD[Int] = rdd2.map(x => {
       println(x + "   map")
       x
     })

     val rdd4: RDD[Int] = rdd3.map(x => {
       println(x + "   map")
       x
     })

     val rdd5: RDD[Int] = rdd4.map(x => {
       println(x + "   map")
       x
     })

    val rdd6: RDD[Int] = rdd5.map(x => {
      println(x + "   map")
      x
    })

    //将rdd2的运算结果缓存
    //val rdd: rdd2.type = rdd2.cache()
    // 第一个行动算子执行时，将rdd2的结果缓存！
    // 默认将RDD2缓存到内存
    //rdd2.cache()
    // rdd2.persist()  等价  rdd2.cache()

    println("-------------------------ck之前的血缘关系------------")

    println(rdd6.toDebugString)

    // 指定缓存的存储级别
    rdd6.checkpoint()

    // 先缓存，checkpoint的Job就可以复用cache
    rdd6.cache()

    // Job1
    rdd6.collect()

    println("-------------------------ck之后的血缘关系------------")

    val rdd7: RDD[Int] = rdd6.map(x => {
      println(x + "   map")
      x
    })

    println(rdd7.toDebugString)

    println("--------------------------------------")

    // Job2
    //  从缓存中取出rdd2,写入文件
    rdd7.saveAsTextFile("output")

    Thread.sleep(100000000)

  }


  /*
      缓存：  提高对重复对象查询的效率

            查询缓存的过程：  ①到缓存中查询，有就返回，没有就查数据库
                            ②缓存一般都使用内存
                            ③缓存都有回收策略
                            ④缓存有失效的情况 (更新了缓存中的对象)


       缓存不会改变血缘关系！

       如果在计算的阶段中产生Shuffle，shuffle之后的数据会自动缓存！在执行时，
	   发现重复执行shuffle之前的阶段，此事会跳过之前
       阶段的执行，直接从缓存中获取数据！

   */

  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    val rdd2: RDD[(Int, Int)] = rdd1.map(x => {
      println(x + "   map")
      (x, 1)
    })

    val rdd3: RDD[(Int, Int)] = rdd2.reduceByKey(_ + _)

    //缓存
    //rdd3.cache()

    rdd3.collect()

    // Job2
    //  从缓存中取出rdd2,写入文件
    rdd3.saveAsTextFile("output")

    Thread.sleep(100000000)

  }

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

    println("-------------------------缓存之前的血缘关系------------")

    println(rdd2.toDebugString)

    // 指定缓存的存储级别
    val rdd3: rdd2.type = rdd2.persist(StorageLevel.MEMORY_AND_DISK)

    println("-------------------------缓存之后的血缘关系------------")

    println(rdd3.toDebugString)

    // Job1
    rdd3.collect()

    println("--------------------------------------")

    // Job2
    //  从缓存中取出rdd2,写入文件
    rdd3.saveAsTextFile("output")

    Thread.sleep(100000000)

  }

  val sc = new SparkContext(new SparkConf()

    // 在scala中获取一个类想Class类型   ：  classof[类型]
    .setAppName("My app")
    .setMaster("local[*]")

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
