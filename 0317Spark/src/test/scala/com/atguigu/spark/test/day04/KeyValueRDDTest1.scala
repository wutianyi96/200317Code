package com.atguigu.spark.test.day04

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, RangePartitioner, SparkConf, SparkContext}
import org.junit._




class  MyPartitioner(nums: Int) extends  Partitioner{

  override def numPartitions: Int = nums

  // 对Person的age进行分区
  override def getPartition(key: Any): Int = {

    if (!key.isInstanceOf[Person1]){
      0
    }else{
      val person: Person1 = key.asInstanceOf[Person1]

      person.age.hashCode() % numPartitions
    }

  }
}

/**
  * Created by VULCAN on 2020/7/14
  */
class KeyValueRDDTest1 {

  /*
  combineByKey

  def combineByKey[C](

      createCombiner: V => C, 使用第一个value制造和value类型不同的zero。
                                使用集合中的一个元素，限通过createCombiner函数，计算得到zeroValue

      mergeValue: (C, V) => C,   // 分区内对value进行聚合
      mergeCombiners: (C, C) => C,  // 分区间对value进行聚合
      partitioner: Partitioner,


      mapSideCombine: Boolean = true,
      serializer: Serializer = null): RDD[(K, C)] = self.withScope {
    combineByKeyWithClassTag(createCombiner, mergeValue, mergeCombiners,
      partitioner, mapSideCombine, serializer)(null)
  }
   */
  @Test
  def test12() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    rdd.aggregateByKey(0)((x,y) => x+y, (x,y) => x+y).saveAsTextFile("output")

    val result: RDD[(Int, Int)] = rdd.combineByKey(
      (value:Int) => 10,
      (x: Int, y: Int) => x + y,
      (x: Int, y: Int) => x + y,
      new HashPartitioner(2)
    )

    result.saveAsTextFile("output4")

  }


  /*
      foldByKey ：  aggregateByKey的seqOp和conbOp一样时，且aggregateByKey的zeroValue类型和 value的类型一致时
                    等价于  foldByKey
   */
  @Test
  def test11() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    rdd.aggregateByKey(0)((x,y) => x+y, (x,y) => x+y).saveAsTextFile("output")

    // 等价上述写法
    rdd.foldByKey(0)( (x,y) => x+y).saveAsTextFile("output2")

  }

  /*
      求每个key对应的平均值
            结果：  (key, avg) = (key, sum / count)
   */
  @Test
  def test10() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)


    val result: RDD[(Int, (Int, Int))] = rdd.aggregateByKey(( 0,0 ))(
      {
        case ((sum, count), value) => (sum + value, count +1)
      },
      {
        case ((sum1, count1), (sum2, count2)) => (sum1 + sum2, count1 + count2)
      })

    //(key, avg)
    val finalResult: RDD[(Int, Double)] = result.mapValues {
      case (sum, count) => sum.toDouble / count
    }

    finalResult.saveAsTextFile("output")


  }

  //分区内同时求最大和最小，分区间合并
  @Test
  def test9() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    /* rdd.aggregateByKey((Int.MinValue,Int.MaxValue))( (zero,value) => (zero._1.max(value),zero._2.min(value)),
       (result1,result2) => (result1._1+result2._1,result1._2+result2._2)
     ).saveAsTextFile("output")*/

    val result: RDD[(Int, (Int, Int))] = rdd.aggregateByKey((Int.MinValue, Int.MaxValue))(
      {
        case (((min, max), value)) => (min.max(value), max.min(value))
      },
      {
        case ((max1, min1), (max2, min2)) => (max1 + max2, min1 + min2)
      })

    result.saveAsTextFile("output")


  }

  //取出每个分区内相同key的最大值然后分区间相加
  @Test
  def test8() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    rdd.aggregateByKey(Int.MinValue)( (zero,value) =>
      zero.max(value),		//分区内value进行运算
      (max1,max2) => max1+max2  //分区间最大value值的运算
    ).saveAsTextFile("output")
  }

  /*
  aggregateByKey:
      aggregateByKey[U: ClassTag](zeroValue: U)(seqOp: (U, V) => U,
      combOp: (U, U) => U): RDD[(K, U)]

      ①对每个key的value进行聚合
      ②seqOp 函数通过 zeroValue U和value V 进行运算 ，返回U类型数据
        在每一个分区内部运算
      ③combOp: 分区间，再对相同key计算的结果合并
      ④zeroValue只在分区间运算时使用！

取出每个分区内相同key的最大值然后分区间相加
分区内同时求最大和最小，分区间合并
求每个key对应的平均值
   */
  @Test
  def test6() : Unit ={

    val list = List((1, 1), (2, 1), (1, 2), (2, 2),
      (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    /*    List( (1,(1,2))  ,  (2,(1,2))  )
        0号区：  key为1： (0+1)+2   =>  (1,3)
                 key为2：   (0+1)+2 =>  (2,3)

        1号区：  key为1：   =>  (1,2)
                 key为2：    =>  (2,2)
                 key为3：   =>  (3,4)

         =====================================
         (1,5),(2,5),(3,4)

     */
    //rdd.aggregateByKey(0)((x,y) => x+y, (x,y) => x+y).saveAsTextFile("output")
    rdd.aggregateByKey("_")((x,y) => x+y, (x,y) => x+y).saveAsTextFile("output")

  }



  /*
      reduceByKey:  对每一个key的values进行reduce操作，在向reduce的结果发给reducer之前，
                      会在mapper的本地执行reduce，类似MR中combiner


                      不能改变RDD中value对应的类型！

               存在shuffle！将计算后的结果，通过 当前RDD的分区器，分当前RDD指定的分区数量！


       reduceByKey和groupByKey的区别？
              功能上： 一个是分组，一个是分组后进行reduce运算！
              性能上：   都有shuffle。
                          reduceByKey 可以在本地聚合，shuffle的数据量小！
                          groupByKey 没有本地聚合，shuffle的数据量大！
   */
  @Test
  def test5() : Unit ={

    val list = List((1, 1), (2, 1), (3, 1),(1, 1), (2, 1), (3, 1),
      (1, 2), (2, 2), (3, 2),(1, 2), (2, 2), (3, 2))

    val rdd: RDD[(Int, Int)] = sc.makeRDD(list, 2)

    val result: RDD[(Int, Int)] = rdd.reduceByKey((x, y) => x + y)

    result.saveAsTextFile("output")

    // 192
    sc.makeRDD(list, 2).reduceByKey((x, y) => x + y).collect()

    // 216
    sc.makeRDD(list, 2).groupByKey().collect()

  }

  @Test
  def test4() : Unit ={

    //mapValues
    val list = List(("a", 1), ("b", 1), ("c", 1))

    val rdd: RDD[(String, Int)] = sc.makeRDD(list, 2)

    val rdd2: RDD[(String, Int)] = rdd.mapValues(value => value * 2)

    println(rdd2.collect().mkString(","))

  }

  @Test
  def test3() : Unit ={

    val list = List(Person1("jack1", 20), Person1("marry", 10), Person1("tom", 30),
      Person1("jack", 21), Person1("marry1", 11), Person1("tom1", 31))

    val rdd: RDD[Person1] = sc.makeRDD(list, 2)

    val rdd2: RDD[(Person1, Int)] = rdd.map(x => (x, 1))

    rdd2.partitionBy(new MyPartitioner(3)).saveAsTextFile("output")

  }



  @Test
  def test2() : Unit ={

    val list = List((1, 1.1), (2, 1.2), (2, 1.1), (1, 1.2), (1, 1.1), (2, 1.2), (2, 1.1), (3, 1.2), (7, 1.1), (10, 1.2), (11, 1.1), (12, 1.2))

    val rdd: RDD[(Int, Double)] = sc.makeRDD(list, 2)


    rdd.partitionBy(new RangePartitioner(3,rdd)).saveAsTextFile("output")

  }

  @Test
  def test1() : Unit ={

    /* val list1 = List(1,2,3,4)

     val rdd1: RDD[Int] = sc.makeRDD(list1, 2)

     rdd1.partition*/

    val list = List((1, 1.1), (2, 1.2), (2, 1.1), (1, 1.2))


    // ParallelCollectionRDD
    val rdd: RDD[(Int, Double)] = sc.makeRDD(list, 2)

    rdd.saveAsTextFile("output")

    // 返回由传入的分区器，分区后的新的RDD
    // 只有key-value类型的RDD可以调用

    /*
          在 object RDD 中：
           implicit def rddToPairRDDFunctions[K, V](rdd: RDD[(K, V)])
    (implicit kt: ClassTag[K], vt: ClassTag[V], ord: Ordering[K] = null): PairRDDFunctions[K, V] = {
    new PairRDDFunctions(rdd)
  }

        传入一个RDD[(K, V)] 自动转为   PairRDDFunctions
        调用 PairRDDFunctions提供的方法，例如partitionBy()
        有隐式转换：  ParallelCollectionRDD  转为  PairRDDFunctions
     */


    /*
       返回由传入的分区器，分区后的新的RDD
       分区器，系统已经提供了默认实现：
          HashPartitioner：
                分区数由主构造传入！
                分区计算，采取key的hashcode % 总的分区数
                      如果为正数，就直接返回
                      如果为负数，控制分区号在  [0,总的分区数)

                 比较两个HashPartitioner的对象时，只比较两个HashPartitioner的总的分区数是否相等，只要相等，就视为两个对象相等！

          RangePartitioner:  按照范围，对RDD中的数据进行分区！ 尽量保证每个分区的数据量是大约相等的！

              def numPartitions: Int = rangeBounds.length + 1

              rangeBounds： 范围边界！

              传入的分区数和最后生成的总的分区数无直接关系！

              局限性： 只能对Key能排序的数据做分区！

     */

    // val list = List((1, 1.1), (2, 1.2), (2, 1.1), (1, 1.2))
    val rdd2: RDD[(Int, Double)] = rdd.partitionBy(new HashPartitioner(4))

    //rdd2.saveAsTextFile("output1")

    // 避免了一次shuffle! 返回rdd2
    rdd2.partitionBy(new HashPartitioner(4)).saveAsTextFile("output1")

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
