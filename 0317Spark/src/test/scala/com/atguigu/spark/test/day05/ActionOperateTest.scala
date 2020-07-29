package com.atguigu.spark.test.day05

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{RangePartitioner, SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/15
  *
  * 1.行动算子和转换算子的区别
  *        转换算子： (transmation Operation)  只是将一个RDD另一个RDD。 是lazy
  *                    只要算子的返回值是RDD，一定是转换算子！
  *        行动算子： (action Operation)  只有执行行动算子，才会触发整个Job的提交！
  *                    只要算子的返回值不是RDD，一是行动算子！
  *
  * 2.reduce
  * 3.collect
  * 4.count
  * 5.first
  * 6.take
  * 7.takeOrdered
  * 8.aggregate
  * 9.fold
  * 10.countByKey
  * 11.countByValue
  * 12.save相关
  * 13.foreach
  * 14.特殊情况
  */
class ActionOperateTest {

  @Test
  def test17() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    // xxxxxx 转换算子

    // 数据量大不建议
    rdd1.collect()

    //让每个executor将结果写入到数据

    // Connection conn=new Connection()
    rdd1.foreach( x=>{
      // 不用闭包
      //Connection conn=new Connection()
      // conn.save(x)

    }  )

    // 一个分区创建一个连接
    rdd1.foreachPartition( x =>{
      // 一个分区调用一次匿名函数，Connection一个分区创建一个
      //Connection conn=new Connection()
      //x.foreach( ele => xxx )
    })

    // 异步，效率快
    //rdd1.foreachPartitionAsync()

  }

  //foreach
  @Test
  def test16() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    // 在driver端
    rdd1.collect().foreach( x=>{

      println(x+Thread.currentThread().getName)

    }  )

    println("-------------------------")

    // 在Executor
    rdd1.foreach( x=>{

      // 本地模式，只有一台worker,master也是自己
      println(x+Thread.currentThread().getName)

    }  )



  }

  //foreach
  @Test
  def test15() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd1: RDD[Int] = sc.makeRDD(list, 2)

    // 在Driver端声明一个变量
    var sum=0

    //遍历RDD中每个元素
    // foreach是算子，算子都是分布式运算，在Executor算
    // 如果有闭包，闭包的变量，会被copy为副本，每个task都复制一份副本
    rdd1.foreach( x => sum += x )

    // 打印的是Driver端的sum
    // 0
    println(sum)

  }

  // save
  @Test
  def test13() : Unit ={

    val list = List(1, 2, 3, 4,5)
    val list2 = List((1,1), (2,1), (3,1),(1,1), (2,1))

    val rdd1: RDD[Int] = sc.makeRDD(list, 3)
    val rdd2: RDD[(Int, Int)] = sc.makeRDD(list2, 3)

    //保存为文本格式文件
    // rdd1.saveAsTextFile()
    // 保存为对象文件(将对象写入文件,对象要求序列化)
    // rdd1.saveAsObjectFile("outputOB")

    // 每个Record 都是key-value,多个Record组成一个block(16k)
    rdd2.saveAsSequenceFile("outputSq")

  }

  // countByValue : 不要求RDD的元素是key-value类型，统计value出现的次数
  @Test
  def test12() : Unit ={

    val list = List(1, 2, 3, 4,5)
    val list2 = List((1,1), (2,1), (3,1),(1,1), (2,1))

    val rdd1: RDD[Int] = sc.makeRDD(list, 3)
    val rdd2: RDD[(Int, Int)] = sc.makeRDD(list2, 3)

    // Map[(key,count)]
    println(rdd2.countByValue())
    println(rdd1.countByValue())

  }

  // countByKey:  基于key的数量进行统计，要求RDD的元素是key-value类型
  @Test
  def test11() : Unit ={

    val list = List(1, 2, 3, 4,5)
    val list2 = List((1,1), (2,1), (3,1),(1,1), (2,1))

    val rdd1: RDD[Int] = sc.makeRDD(list, 3)
    val rdd2: RDD[(Int, Int)] = sc.makeRDD(list2, 3)

    // Map[(key,count)]
    println(rdd2.countByKey())

  }

  // fold : 简化版aggregate。 要求 zeroValue必须和RDD中的元素类型一致，且分区间和分区内运算逻辑一致
  @Test
  def test10() : Unit ={

    val list = List(1, 2, 3, 4,5)

    val rdd: RDD[Int] = sc.makeRDD(list, 3)

    val result: Int = rdd.fold(10)(_ + _)

    println(result)

  }

  //aggregate:  和aggregateByKey类似，不要求数据是KEY-VALUE，zeroValue既会在分区内使用，还会在分区间合并时使用
  @Test
  def test9() : Unit ={

    val list = List(1, 2, 3, 4,5)

    val rdd: RDD[Int] = sc.makeRDD(list, 3)

    val result: Int = rdd.aggregate(10)(_ + _, _ + _)

    println(result)

  }

  // takeOrdered : 先排序，再取前n
  @Test
  def test8() : Unit ={

    val list = List(9, 2, 3, 4,5,6)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    println(rdd.takeOrdered(4).mkString(","))

  }

  // take: 取RDD中的前n个元素 ,先从0号区取，不够再从1号区
  @Test
  def test7() : Unit ={

    val list = List(1, 2, 3, 4,5,6)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    println(rdd.take(4).mkString(","))

  }

  // first : 取RDD中的第一个元素   没有对RDD重新排序，0号区的第一个
  @Test
  def test6() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    println(rdd.first())

  }

  // count ： 统计RDD中元素的个数
  @Test
  def test5() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    println(rdd.count())

  }

  @Test
  def test4() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    // collect 将运算结果收集到driver端，在driver端遍历结算结果，打印
    // 如果计算的结果数据量大，driver端就OOM
    rdd.collect().foreach(println)

    //

  }

  @Test
  def test3() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    println(rdd.reduce(_ + _))


  }

  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val rdd2: RDD[(Int, Int)] = rdd.map(x => {
      println(x)
      (x, 1)
    })

    // RangePartitioner在抽样时,将抽样的结果进行collect(),触发的
    val partitioner = new RangePartitioner(3, rdd2)

    //val result: RDD[(Int, Int)] = rdd2.partitionBy(partitioner)

    // sortBy在运行时，触发了行动算子，sortBy本身是一个转换算子
    // RangePartitioner在抽样时触发的
    //val rdd2: RDD[Int] = rdd1.sortBy(x => x)

  }


  // 演示行动算子和转换算子
  @Test
  def test() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val rdd1: RDD[Int] = rdd.map(x =>{
      println(x)
      x
    })

    rdd1.saveAsTextFile("output")

    rdd1.collect()

  }

  val sc = new SparkContext(new SparkConf().setAppName("My app").setMaster("local[*]"))

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
