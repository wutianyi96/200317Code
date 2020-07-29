package com.atguigu.spark.test.day04

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/14
  */


// 样例类
case class Person(name:String,age:Int)

case class Person1(name:String,age:Int) extends  Ordered[Person1] {
  // 升序比较name
  override def compare(that: Person1): Int = this.name.compareTo(that.name)
}

class SingeValueRDDTest1 {

  @Test
  def test2() : Unit ={

    val list = List(Person1("jack", 20), Person1("marry", 10), Person1("tom", 30))

    val rdd: RDD[Person1] = sc.makeRDD(list, 2)

    rdd.sortBy(p=>p,false).saveAsTextFile("output")

  }

  /*
      scala中提供了Ordering:  extends Comparator[T]
                      比较器。比较两个对象时，通过一个比较器的实例，调用的是比较器中的compareTo方法！

                    Ordered: with java.lang.Comparable[A]
                        当前的类是可排序的，当前类已经实现了排序的方法，在比较时，直接调用类的compareTo方法！
   */
  @Test
  def test1() : Unit ={

    val list = List(Person("jack", 20), Person("marry", 10), Person("tom", 30))

    val rdd: RDD[Person] = sc.makeRDD(list, 2)

    rdd.sortBy( p => p.age,false ).saveAsTextFile("output")

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
