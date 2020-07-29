package com.atguigu.spark.test.day05

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark._
import org.junit._

case class Person1(name:String,age:Int)

case class Person(name:String,age:Int) extends  Ordered[Person]{
  // 按照名称升序排
  override def compare(that: Person): Int = this.name.compareTo(that.name)
}
/**
  * Created by VULCAN on 2020/7/14
  *
  * join
  * 如果key存在不相等呢？
  *
  * leftOuterJoin
  *
  * cogroup
  */
class KeyValueRDDTest1 {

  //cogroup: 先每个RDD内部，根据key进行聚合，将相同key的value聚合为Iterable，再在rdd之间，根据相同key聚合
  @Test
  def test7() : Unit ={

    //  (1,List("a","aa"))
    val list1 = List((1, "a"), (2, "b"), (3, "c"),(5, "e"),(1, "aa"), (2, "bb"))

    //  (1,List("a1"))
    val list2 = List((1, "a1"), (2, "b1"), (3, "c1"),(4, "d1"),(3, "c11"),(4, "d11"))

    val rdd1: RDD[(Int, String)] = sc.makeRDD(list1, 2)
    val rdd2: RDD[(Int, String)] = sc.makeRDD(list2, 2)

    // (1,(List("a","aa"),List("a1")))
    rdd1.cogroup(rdd2).saveAsTextFile("output")

    /*0号分区
    (4,(CompactBuffer(),CompactBuffer(d1, d11)))
    (2,(CompactBuffer(b, bb),CompactBuffer(b1)))*/

    /*1号分区
    (1,(CompactBuffer(a, aa),CompactBuffer(a1)))
    (3,(CompactBuffer(c),CompactBuffer(c1, c11)))
    (5,(CompactBuffer(e),CompactBuffer()))*/

  }


  /*
      leftJoin : 取左侧数据的全部和右侧有关联的部分

          所有的Join都有shuffle
   */
  @Test
  def test6() : Unit ={

    /*
       (2,(b,Some(b1))) :  Some代表有，None代表无
          Option :  选择
     */
    val list1 = List((1, "a"), (2, "b"), (3, "c"),(5, "e"))
    val list2 = List((1, "a1"), (2, "b1"), (3, "c1"),(4, "d1"))

    val rdd1: RDD[(Int, String)] = sc.makeRDD(list1, 2)
    val rdd2: RDD[(Int, String)] = sc.makeRDD(list2, 2)

    rdd1.leftOuterJoin(rdd2).saveAsTextFile("output")
    //rdd1.rightOuterJoin(rdd2).saveAsTextFile("output1")

    rdd1.fullOuterJoin(rdd2).saveAsTextFile("output2")


  }

  /*
      join : 不同RDD中，将key相同的value进行关联

          有shuffle
   */
  @Test
  def test5() : Unit ={

    val list1 = List((1, "a"), (2, "b"), (3, "c"),(5, "e"))
    val list2 = List((1, "a1"), (2, "b1"), (3, "c1"),(4, "d1"))

    val rdd1: RDD[(Int, String)] = sc.makeRDD(list1, 2)
    val rdd2: RDD[(Int, String)] = sc.makeRDD(list2, 2)

    rdd1.join(rdd2).saveAsTextFile("output")


  }

  @Test
  def test4() : Unit ={

    val list = List(Person1("jack",20), Person1("jack",21), Person1("tom",20), Person1("marry",20))
    val rdd: RDD[Person1] = sc.makeRDD(list, 2)

    // 转为key-value
    val rdd2: RDD[(Int, Person1)] = rdd.map(p => (p.age, p))

    val result: RDD[(Int,Person1)] = rdd2.sortByKey(false)

    result.saveAsTextFile("output")

  }

  /*
       private val ordering = implicitly[Ordering[K]]

       提供一个隐式的Ordering[K]的比较器
   */
  @Test
  def test3() : Unit ={

    //提供隐式变量，供sortByKey使用
    implicit val ord: Ordering[Person1] = new Ordering[Person1] {
      // 比较策略   name升序，相同name，age降序
      override def compare(x: Person1, y: Person1): Int = {
        val result: Int = x.name.compareTo(y.name)
        if (result == 0) {
          -x.age.compareTo(y.age)
        } else {
          result
        }
      }
    }

    val list = List(Person1("jack",20), Person1("jack",21), Person1("tom",20), Person1("marry",20))
    val rdd: RDD[Person1] = sc.makeRDD(list, 2)

    // 转为key-value
    val rdd2: RDD[(Person1, Int)] = rdd.map((_, 1))

    val result: RDD[(Person1, Int)] = rdd2.sortByKey()

    result.saveAsTextFile("output")


  }

  @Test
  def test2() : Unit ={

    val list = List(Person("jack",20), Person("jack",21), Person("tom",20), Person("marry",20))
    val rdd: RDD[Person] = sc.makeRDD(list, 2)

    // 转为key-value
    val rdd2: RDD[(Person, Int)] = rdd.map((_, 1))

    val result: RDD[(Person, Int)] = rdd2.sortByKey()

    result.saveAsTextFile("output")

  }

  /*
      sortByKey: 根据key进行排序

        def sortByKey(ascending: Boolean = true, numPartitions: Int = self.partitions.length)
      : RDD[(K, V)]

      要求排序的key的类型，必须是可以排序的Ordered类型！
   */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val rdd2: RDD[(Int, Int)] = rdd.map((_, 1))

    val result: RDD[(Int, Int)] = rdd2.sortByKey(false)

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
