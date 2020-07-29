package com.atguigu.spark.test.day06

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

import scala.collection.mutable

/**
  * Created by VULCAN on 2020/7/17
  *      累加器:  场景： 计算，累加
  *              可以给累加器起名称，可以在WEB UI查看每个Task中累加器的值，以及汇总后的值！
  *               官方只提供了数值类型(Long，Double)的累加器，用户可以通过实现接口，自定义！
  *
  *               获取官方累加器：  SparkContext.LongAccomulator
  *
  *               Task：  调用add进行累加，不能读取值！
  *               Driver:  调用value获取累加的值
  *
  *              自定义：  实现AccumulatorV2
  *              必须实现的核心方法：  ①add  : 累加值
  *                                  ②reset ： 重置累加器到0
  *                                  ③merge:  合并其他累加器到一个累加器
  *
  *                     创建： new
  *                            还需要调用 sparkContext.register() 注册
  *                调用行动算子，才会实现真正的累加，不会重复累加，即便是task重启！
  *
  *
  *                累加器在序列化到task之前： copy()返回当前累加器的副本 ---> reset()重置为0 ---->isZero(是否归0)
  *                                    只有在isZero返回true，此时才会执行序列化！
  *                                    false就报错！
  *
  *                                  目的为了保证累加器技术的精确性！
  *
  *
  *                 wordcount(单词，1)(单词，1) 可以使用累加器解决！
  *                 好处：避免了shuffle，在Driver端聚合！
  *
  *
  *                 注意：  序列化到task之前，要返回一个归0累加器的拷贝！
  *
  *
  *
  *      广播变量：  允许将广播变量发给给每一个机器，而不是拷贝拷贝给每一个task!
  *
  *                使用场景：  需要在多个task中使用共同的大的只读的数据集！
  *
  *                作用：  节省网络传输消耗！
  *
  */

class WordCountAccumulator extends AccumulatorV2[String,mutable.Map[String,Int]] {

  //存放累加结果的集合
  private var wordMap: mutable.Map[String, Int] =mutable.Map[String, Int]()

  //判断是否为空
  override def isZero: Boolean = wordMap.isEmpty

  // 复制
  override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = new WordCountAccumulator()

  //重置归0
  override def reset(): Unit = wordMap.clear()

  // 累加
  override def add(v: String): Unit = {

    //累加的单词
    // val word =v

    //累加单词时，更新wordMap    acc.add("hello")  => wordMap: {("hello",N)  }  N每次累加1
    wordMap.put(v, wordMap.getOrElse(v,0)+1 )

  }

  //合并其他累加器的值到一个累加器
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {

    //将两个累加器的wordMap合并到一个中

    // 获取要合并的累加器中的Map
    val toMergeMap: mutable.Map[String, Int] = other.value
    //遍历toMergeMap,只不过elem是entry间接对,可以用模式匹配(word,count)
    for ((word,count) <- toMergeMap) {

      wordMap.put(word,wordMap.getOrElse(word,0)+count )
    }

    /*或者：
        wordMap = map1.foldLeft(map2)(
        ( innerMap, kv ) => {
          innerMap(kv._1) = innerMap.getOrElse(kv._1, 0) + kv._2  //更新innerMap
          innerMap  //返回innerMap
    */

  }

  // 获取累加后的值
  override def value: mutable.Map[String, Int] = wordMap
}

class OtherDataModeTest {

  /*
       广播变量的使用
   */
  @Test
  def test6() : Unit ={

    val list1 = List(1, 2, 3, 4,5,6,7,8,9,10)

    // 假设很大
    val list2 = List(5,6,7,8,9,10)

    val list2Bc: Broadcast[List[Int]] = sc.broadcast(list2)

    val rdd: RDD[Int] = sc.makeRDD(list1, 4)

    // 使用广播变量
    val rdd2: RDD[Int] = rdd.filter(x => list2Bc.value.contains(x))

    rdd2.collect().foreach(println)

  }


  /*
      广播变量的引入
   */
  @Test
  def test5() : Unit ={

    val list1 = List(1, 2, 3, 4,5,6,7,8,9,10)

    // 假设很大
    val list2 = List(5,6,7,8,9,10)

    val rdd: RDD[Int] = sc.makeRDD(list1, 4)

    // 按照list2来过滤 list1中的元素 list2是一个闭包变量，复制闭包变量的每一个副本到每一个task!
    rdd.filter(x=>list2.contains(x) )

  }

  /*
      使用自定义累加器
   */
  @Test
  def test4() : Unit ={

    val list = List("hello", "hello", "hi", "hi", "hello")

    val rdd: RDD[String] = sc.makeRDD(list, 2)

    //创建累加器
    val acc = new WordCountAccumulator

    //注册
    sc.register(acc,"wordcount")

    //使用
    rdd.foreach( word => acc.add(word))

    //获取结果
    println(acc.value)


  }

  /*
      使用累加器获取累加的值
   */
  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)
    // 获取官方提供的Long类型的累加器
    val acc: LongAccumulator = sc.longAccumulator("mySum")

    //在driver端先加10
    acc.add(10)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    // 闭包，累加器以副本的形式发送到每个task
    rdd.foreach(x=> acc.add(x) )

    //Driver中获取累加器的值
    println(acc.value)

    Thread.sleep(100000000)

  }


  /*
      引入案例
   */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)


    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    var sum:Int =0
    // 构成闭包后，sum会以副本的形式复制发送到每一个task
    rdd.foreach(x=> sum+=x )

    //Driver中的sum : 0
    println(sum)

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
