package com.atguigu.spark.test.day01

/**
  * Created by VULCAN on 2020/7/11
  */

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._
class RDDTest {


  /*
       makeRDD(Seq[T],numSplice):  基于Seq，创建一个 ParallelCollectionRDD

       等价于
       parallelize(Seq[T],numSplice)

   */
  @Test
  def test1() : Unit ={

    val conf = new SparkConf().setAppName("My app").setMaster("local")

    val sparkContext = new SparkContext(conf)

    // 从内存中创建RDD
    val list = List(1, 2, 3, 4, 5)

    // val rdd1: RDD[Int] = sparkContext.makeRDD(list)

    val rdd2: RDD[Int] = sparkContext.parallelize(list)

    println(rdd2.collect().mkString(","))


    sparkContext.stop()

  }


  /*
       numSlices: Int = defaultParallelism : 默认的并行度。同时允许多少个task同时计算！
                    会创建多少个分区！

       defaultParallelism =  scheduler.conf.getInt("spark.default.parallelism", totalCores)
                尝试从配置中获取spark.default.parallelism，如果用户没有配置，使用totalCores
                作为默认值。

                totalCores： 当前环境的cpu的核数
                    Excutor: 申请了10个CPU，  totalCores=10


      defaultParallelism默认就是当前Excutor中的核数！

          RDD可分区，可分区的目的是为了并行计算！
          RDD的一个分区，可以使用一个task进行计算！


          setMaster("local"): 向本地的master申请资源
              local : 默认1核
              local[2] : 2核      默认2个区
                                    0号区： 1，2
                                    1号区： 3，4，5
              local[4] : 4核       默认4个区
                                    0号区： 1
                                    1号区： 2
                                    2号区   3
                                    3号区   4，5
              local[*] : 越多越好

       默认是根据task的并行度，cpu的核数，为RDD自动进行分区！


       sparkContext.makeRDD(list,2): 手动指定只需要分2个区

   */
  @Test
  def test2() : Unit ={

    // 提前删除output  fileSystem: 本地文件系统
    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path("output")

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

    val conf = new SparkConf().setAppName("My app").setMaster("local")

    // println(conf.getInt("spark.default.parallelism", 4))

    val sparkContext = new SparkContext(conf)

    val list = List(1, 2, 3, 4, 5)

    val rdd1: RDD[Int] = sparkContext.makeRDD(list,2)

    // 执行将结果保存到文件的行动算子
    // output目录必须不能存在否则就报错
    rdd1.saveAsTextFile(path.getName)

    sparkContext.stop()

  }

  /*
      分区规则： 不同的RDD分区策略是不同的！需要看具体的实现

      现象： 1，2，3，4，5
      默认2个区
                                    0号区： 1，2
                                    1号区： 3，4，5

        默认3个区：
                                     0号区： 1
                                    1号区： 2，3
                                    2号区：  4，5

       默认4个区
                                    0号区： 1
                                    1号区： 2
                                    2号区   3
                                    3号区   4，5


   ParallelCollectionRDD: 对几个集合中的数据进行平均分配到若干个分区，如果出现不能平均分配
                            后面的分区数据量会略多！



    }
   */

  @Test
  def test3() : Unit ={

    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path("output")

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

    val conf = new SparkConf().setAppName("My app").setMaster("local")

    val sparkContext = new SparkContext(conf)

    val list = List(1, 2, 3, 4, 5)

    val rdd1: RDD[Int] = sparkContext.makeRDD(list,3)

    rdd1.saveAsTextFile(path.getName)

    sparkContext.stop()

    sparkContext.stop()

  }

  @Test
  def test4() : Unit ={

    val ints: Array[Int] = Array(1, 2, 3, 4, 5)

    // index :[0,1)    ele: 1
    println(ints.slice(0, 1).mkString(","))
    // [1,3) ele: 2,3
    println(ints.slice(1, 3).mkString(","))
    // 4,5
    println(ints.slice(3, 5).mkString(","))
    /*
    1
    2,3
    4,5*/
  }

  @Test
  def test5() : Unit ={

    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path("output")

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

    val conf = new SparkConf().setAppName("My app").setMaster("local")

    val sparkContext = new SparkContext(conf)

    /*
         只指定目录名： 目录下所有的文件都会被读到！
                       要求目录中不能再嵌套子目录

                指定只读目录中的某个文件: input/hello/Hello1.txt

                使用占位符：input/Hello*.txt

             textFile: 返回HadoopRDD,分区和CPU的核数（task并行度）关系不大！

             numSplits(期望切片数)=  math.min(defaultParallelism, 2)
                   不等于实际切片数！

            在实际大数据处理时，一般都是一块切一片，有多少块，就有多少片，就有多少个分区！


     */
    val rdd: RDD[String] = sparkContext.textFile("input/Hello*.txt")

    rdd.saveAsTextFile(path.getName)

    sparkContext.stop()

  }

}
