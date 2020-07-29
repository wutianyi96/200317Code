package com.atguigu.spark.test.day03

/**
  * Created by VULCAN on 2020/7/13
  *
  * flatMap
  * 小功能：将List(List(1,2),3,List(4,5))进行扁平化操作
  *
  * glom
  * 小功能：计算所有分区最大值求和（分区内取最大值，分区间最大值求和）
  *
  * filter
  * 小功能：从服务器日志数据apache.log中获取2015年5月17日的请求路径
  *
  * groupby
  * 将List("Hello", "hive", "hbase", "Hadoop")根据单词首写字母进行分组
  * 小功能：从服务器日志数据apache.log中获取每个时间段(不考虑日期)访问量。
  * 小功能：WordCount
  *
  *
  */
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._
class SingleValueRDDTest2 {

  /*
    小功能：从服务器日志数据apache.log中获取每个时间段(不考虑日期)访问量。
   */
  @Test
  def test8() : Unit ={

    val rdd: RDD[String] = sc.textFile("input/apache.log",1)

    // 按照时间段和当前行进行分组
    val rdd1: RDD[(String, Iterable[String])] = rdd.groupBy(line => line.split(" ")(3).substring(11, 13))

    // 求每个时间端的访问量
    rdd1.map{

      case (time,iter) => (time,iter.size)

    }.saveAsTextFile("output")


  }

  /*
      将List("Hello", "hive", "hbase", "Hadoop")根据单词首写字母进行分组
 *
 * 小功能：WordCount
   */
  @Test
  def test7() : Unit ={

    val list = List("Hello", "hive", "hbase", "Hadoop")

    // sc.makeRDD(list,2).groupBy(x=> x.charAt(0))
    sc.makeRDD(list,2).groupBy(x=> x(0)).saveAsTextFile("output")

  }

  /*
      shuffle:  洗牌！  MR中通过洗牌将map处理的数据在经过reduce处理之前，整理有序！

                Hadoop中MR的执行过程：  Map  ------Shuffle -------Reduce
                shuffle 横跨MapTash和ReduceTask，既有Map端的shuffle，还有Reduce端的shuffle！

            MapTask：     Inputformat.RecoreReader-- 将数据封装为 Mapper处理的 KEYIN-VALUEIN -- Mapper.map()---write(KEYOUT,VALUEOUT)

                    MapTask端的shuffle      -----分区--------排序---------合并----------结果文件(若干区，每个区数据有序)

           ReduceTask:    启动ShuffleConsumerPlugin-----copy(将多个MapTask同一个分区的数据拷贝，网络IO)------merge(将同一分区在reduce前
           整体有序) --------reduce(  )


           MR为什么慢： shuffle过程中有大量的磁盘IO，网络IO 。 应该尽量避免shuffle! 优化，减少磁盘IO和网络IO

                        减少磁盘IO： MapTask少溢写。  增大缓冲区大小！
                                    增加merge时，读取片段的数量。
                                    使用Combiner(溢写前执行，合并时segement处理超过3)

                         减少网络IO：  压缩。
                                      特殊情况例如ETL，不用reduce(排序和合并)!



             Spark中也有Shuffle: 将之前的分区的数据，打乱后重新分区！
                                 只要产生shuffle，一定会有磁盘IO！
                                 只要删除shuffle，一定产生一个新的stage!

                         Job 有N个Stage ，一个Stage将每一次的RDD转换创建为一个Task!

                         spark的shuffle不会对数据进行排序！

                       哪些算子会造成shuffle ： ①重新分区的操作： repartition and coalesce,
                                               ② xxxByKey
                                               ③对分区进行关联的操作：  cogroup and join.


                        如何完成shuffle : 提供一组mapTask，组织数据
                                          提供一组reduceTask,聚合数据

                                          map Task和reduce Task术语来自MR，和 map，reduce算子无关！


                    过程： map Task 将数据组织在内存，一旦内存满了，就溢写到磁盘，会对数据进行分区和排序(取决于设置)
                          reduce Task读取相关分区的数据

                          shuffle期间，如果一个大的RDD一直被引用，此时会在磁盘产生很多临时文件！临时文件会一直保留到
                           RDD被回收！

                   总结： Sparkde的shuffle和hadoop的shuffle的目的一样！
                          实现的原理也一样，遵循MR模型。
                          细节不同(溢写，分区，排序)！


   */

  /*
      groupby : 分组。
                 使用传入的f函数对分区中的T类型计算，计算后的类型可以和T不一致！
                 将计算后的结果进行分组，以计算后的结果作为key,将对应的T作为value分组！


                 有shuffle!


     def groupBy[K](
      f: T => K,
      numPartitions: Int)(implicit kt: ClassTag[K]): RDD[(K, Iterable[T])]


      只要看到一个算子，提供了numPartitions：Int参数，代表这个算子可能会产生shuffle!

      groupby分组后，默认分区数不变，数据会被重新分区！

   */
  @Test
  def test6() : Unit ={

    val list = List(1,2,3,4,5,6,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 4)

    //采用默认分区数
    //val result: RDD[(Int, Iterable[Int])] = rdd.groupBy(x => x)

    // 手动指定重新分几个区
    val result: RDD[(Int, Iterable[Int])] = rdd.groupBy(x => x,2)

    //sc.makeRDD(list, 4).groupBy(x => x,2).saveAsTextFile("output")

    result.saveAsTextFile("output")

  }

  //小功能：从服务器日志数据apache.log中获取2015年5月17日的请求路径
  @Test
  def test5() : Unit ={

    val rdd: RDD[String] = sc.textFile("input/apache.log",1)

    // 过滤出2015年5月17日的数据
    val r1: RDD[String] = rdd.filter(line => line.split(" ")(3).contains("17/05/2015"))

    // 获取请求路径
    val result: RDD[String] = r1.map(line => line.split(" ")(6))

    result.saveAsTextFile("output")


  }

  /*
      filter ： 过滤！ 不会改变分区数！
   */
  @Test
  def test4() : Unit ={

    val list = List(1,2,3,4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val result: RDD[Int] = rdd.filter(x => x % 2 == 1)

    result.saveAsTextFile("output")

  }

  /*
      小功能：计算所有分区最大值求和（分区内取最大值，分区间最大值求和）
   */
  @Test
  def test3() : Unit ={

    val list = List(1,2,3,4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    /*// mapPartition
    // 求分区的最大值
    val r1: RDD[Int] = rdd.mapPartitions(iter => List(iter.max).iterator)

    // 求和
    // 所有的统计操作： sum(),avg(),max(),min(),count() 都是行动算子
    println(r1.sum())*/


    //将一个分区数据合并为数组
    val r1: RDD[Array[Int]] = rdd.glom()

    //求数组中最大值
    val r2: RDD[Int] = r1.map(array => array.max)

    println(r2.sum())




  }


  /*
      glom():  将一个分区的所有元素合并到一个Array中
   */
  @Test
  def test2() : Unit ={

    val list = List(1,2,3,4)

    val rdd: RDD[Any] = sc.makeRDD(list, 2)

    val result: RDD[Array[Any]] = rdd.glom()

    //  result.collect() => Array [ Array[Any],Array[Any] ]
    result.collect().foreach(x=>println(x.mkString(",")))

  }

  /*
        flatMap : 先map再扁平化。不会改变分区和分区逻辑！

        将List(List(1,2),3,List(4,5))进行扁平化操作


   */
  @Test
  def test() : Unit ={

    val list = List(List(1, 2), 3, List(4, 5))

    val rdd: RDD[Any] = sc.makeRDD(list, 2)

    val result: RDD[Any] = rdd.flatMap {
      // 将单个Int,转成集合
      case x: Int => List(x)
      case y: List[_] => y

    }

    result.saveAsTextFile("output")

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
