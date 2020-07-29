package com.atguigu.spark.test.day05

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/15
  */
class LineageTest {

  /*
        血缘关系： 在stage执行时，让task根据血缘来计算
                  在容错时，根据血缘关系恢复数据！

        依赖：  主要是为了让DAG调度器，划分stage
                  最开始只有一个窄依赖的stage
                  每多一个宽依赖，就再生成一个stage

                  stage数量=  初始stage(1) + 宽依赖的数量
                    当stage完成划分时，就会发生shuffle，影响性能！

                stage的数量看宽依赖的数量： 宽依赖数量 + 1
                task的数量：  分区的数据

   */
  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    val rdd2: RDD[ Int] = rdd1.map(x=>x)

    val rdd3: RDD[(Int, Int)] = rdd2.map((_, 1))

    val rdd4: RDD[(Int, Iterable[Int])] = rdd3.groupByKey()

    rdd4.collect()

    rdd4.saveAsTextFile("output")

    Thread.sleep(100000000)



  }


  /*
        stage的划分：
   */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)

    // rdd1: ParallelCollectionRDD
    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    // 天生  List()
    println(rdd1.dependencies)

    // rdd2: MapPartitionsRDD
    val rdd2: RDD[(Int, Int)] = rdd1.map((_, 1))

    //List(org.apache.spark.OneToOneDependency@e4b6f47)  1对1依赖 (窄依赖)
    println(rdd2.dependencies)

    // rdd3: ShuffledRDD
    val rdd3: RDD[(Int, Iterable[Int])] = rdd2.groupByKey()

    // 获取血缘关系 ，为了方便根据血缘关系在task崩溃时，重建数据
    //println(rdd3.toDebugString)

    // List(org.apache.spark.ShuffleDependency@4d192aef)  shuffle依赖，宽依赖
    println(rdd3.dependencies)


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
