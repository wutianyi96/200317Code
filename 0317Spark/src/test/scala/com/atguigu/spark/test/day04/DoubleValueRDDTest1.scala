package com.atguigu.spark.test.day04

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/14
  *
  *    双value: 两个单value的RDD。理解为两个RDD集合进行转换！
  *
  * intersection： 交集
  * 如果两个RDD数据类型不一致怎么办？
  *
  *
  * union： 并集
  * 如果两个RDD数据类型不一致怎么办？
  *
  * subtract： 差集
  * 如果两个RDD数据类型不一致怎么办？
  *
  * cartesian
  *
  * zip
  */
class DoubleValueRDDTest1 {


  /*
      zip: 和scala的zip一样，返回两个RDD相同位置的元素组成的key-value对。
              假设两个RDD的分区数一致，且分区中元素个数也一致！

              相同分区号互拉，相同位置拉链！

如果两个RDD数据类型不一致怎么办？ 不影响！
如果两个RDD数据分区不一致怎么办？ 报错！ 要求分区个数必须一致！
如果两个RDD分区数据数量不一致怎么办？ 报错！要求元素数量一样！


      无shuffle!

zipWithIndex
zipPartitions

   */

  @Test
  def test9() : Unit ={

    /*
        zipPartitions : 将当前RDD的T类型和其他RDD的U类型，通过函数进行操作，之后返回任意类型！
     */
    val list1 = List(1, 2, 3,4.0,5)
    val list2 = List(5, 6, 8,9)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 2)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 2)

    //  zipAll 是scala集合中提供的
    rdd1.zipPartitions(rdd2)( (it1,it2) => it1.zipAll(it2,10,20) ).saveAsTextFile("output")

  }

  @Test
  def test8() : Unit ={

    val list1 = List(1, 2, 3,4.0)

    val rdd: RDD[Double] = sc.makeRDD(list1, 2)

    // 当前rdd的每个元素和元素的索引拉链
    rdd.zipWithIndex().saveAsTextFile("output")

  }

  @Test
  def test7() : Unit ={

    val list1 = List(1, 2, 3,4.0)
    val list2 = List(5, 6, 8,9)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 2)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 2)

    rdd1.zip(rdd2).saveAsTextFile("output")
    // res1.zip(res2).saveAsTextFile("output")
    /*
    (1.0, 5)
    (2.0, 6)
    */
    /*
    (3.0, 8)
    (4.0, 9)
    */

  }

  /*
      cartesian: 笛卡尔集
            返回的数据是 (a，b), 返回的RDD的分区个数为  两个RDD分区的乘积！

            分区逻辑是ParallelCollectionRDD分区逻辑！


   */
  @Test
  def test6() : Unit ={

    val list1 = List(1, 2, 3, 4.0)
    val list2 = List(5, 6, 3, 4)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 3)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 4)

    rdd1.cartesian(rdd2).saveAsTextFile("output")

  }



  /*
      subtract： 差集。 以当前this RDD的分区器和分区大小为准！
            this.substact(other)

       有shuffle!
   */
  @Test
  def test4() : Unit ={

    val list1 = List(1, 2, 3, 4.0)
    val list2 = List(5, 6, 3, 4)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 3)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 4)

    rdd1.subtract(rdd2).saveAsTextFile("output")

  }

  /*
      List(1,2,3)    List(1,2,3)
          数据操作：  union ： (1,2,3,1,2,3)
           数学集合：  union :  (1,2,3)
       union： 并集
              将所有RDD的分区汇总！ 不会有shuffle!除非，union.distinct

    * 如果两个RDD数据类型不一致怎么办？
            有泛型约束！
   */
  @Test
  def test3() : Unit ={

    val list1 = List(1, 2, 3, 4.0)
    val list2 = List(5, 6, 3, 4)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 3)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 4)

    // 先将两个RDD的所有分区汇总
    rdd1.union(rdd2).saveAsTextFile("output")



  }

  /*
      defaultPartitioner(self, other) : 返回一个分区器！
                                           将当前RDD和要进行操作的N个RDD一起传入，获取这些RDD操作后，要使用的分区器！

                      numPartitions： 如果设置了 spark.default.parallelism，就使用它作为总的分区数！
                                      如果没有设置spark.default.parallelism，就使用上游的最大值！

                       分区逻辑上：  默认使用numPartitions最大的上游RDD的分区策略，如果不可用，默认使用
                                    HashParttioner

  abstract class Partitioner extends Serializable {
  def numPartitions: Int  ： 当前分区器一共有几个分区！
  def getPartition(key: Any): Int  ： 计算K所属的分区！
}
   */


  /*
      intersection： 交集  ，会造成shuffle！
                      最终会以 上游RDD中分区数大的RDD的分区数作为最终的结果输出的分区数！

       只要是Key-value类型，默认就使用HashParttioner分区！
            输出结果：  分区总数 ：上游RDD中分区数大的RDD的分区数
                        怎么分：  HashPartitoiner对key做分区！



 * 如果两个RDD数据类型不一致怎么办？
            有泛型约束！两个集合的泛型必须一致！

             如果可以运行，做交集运算时，要求类型和值必须一致的！
   */
  @Test
  def test1() : Unit ={

    val list1 = List(1, 2, 3, 4.0)
    val list2 = List(5, 6, 3, 4)

    val rdd1: RDD[AnyVal] = sc.makeRDD(list1, 2)
    val rdd2: RDD[AnyVal] = sc.makeRDD(list2, 4)

    rdd1.intersection(rdd2).saveAsTextFile("output")

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
