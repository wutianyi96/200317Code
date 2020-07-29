package com.atguigu.spark.test.day07

/**
  * Created by VULCAN on 2020/7/20
  */

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.junit._

case class User(name:String,age:Int)

case class User1(name:String,salary:Double)
class DataSetTest {

  /*
      DF和DS的区别
   */
  @Test
  def test8() : Unit ={

    import sparkSession.implicits._

    val df: DataFrame = sparkSession.read.json("input/employees.json")

    //使用df读取数据的第一列  DataSet[Row]
    val ds: Dataset[Int] = df.map(row => row.getInt(0))

    // 在运行时出错
    //ds.show()

    // 使用DS，强类型，在编译时就进行检查
    val ds1: Dataset[User1] = df.as[User1]

    val ds2: Dataset[String] = ds1.map(user1 => user1.name)

    ds2.show()



  }

  /*
      由DF转DS

        Encoder:  将一个Java的对象，转为DF或DS结构时，需要使用的编码器！
                  在创建DS或DF时，系统会自动提供隐式的Encoder自动转换！
                  导入 sparkSesson.implicits._
   */

  @Test
  def test6() : Unit ={

    import sparkSession.implicits._
    // 必须是一个Seq
    //  Dataset[Row]
    val df: DataFrame = List(User("jack", 20), User("jack1", 20), User("jack2", 20)).toDF()

    //  Dataset[Row]  =>   Dataset[User]
    val ds: Dataset[User] = df.as[User]

    ds.show()

  }


  /*
      借助样例类创建DS(最常使用)

   */
  @Test
  def test5() : Unit ={

    import sparkSession.implicits._
    // 必须是一个Seq
    val ds: Dataset[User] = List(User("jack", 20), User("jack1", 20), User("jack2", 20)).toDS()

    ds.show()

  }


  /*
      DS 转 DF
   */
  @Test
  def test4() : Unit ={

    import sparkSession.implicits._
    // 必须是一个Seq
    val ds: Dataset[Int] = List(1, 2, 3, 4).toDS()

    // df本质就是ds，是 DS[ROW]类型的DS
    val df: DataFrame = ds.toDF()

  }

  /*
      DS转RDD
   */
  @Test
  def test3() : Unit ={

    import sparkSession.implicits._
    // 必须是一个Seq
    val ds: Dataset[Int] = List(1, 2, 3, 4).toDS()

    // rdd就是DS的一个属性
    val rdd: RDD[Int] = ds.rdd


  }

  /*
      RDD转DS
   */
  @Test
  def test2() : Unit ={

    import sparkSession.implicits._

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sparkSession.sparkContext.makeRDD(list, 2)

    rdd.toDS().show()

  }

  @Test
  def test1() : Unit ={

    import sparkSession.implicits._
    // 必须是一个Seq
    val ds: Dataset[Int] = List(1, 2, 3, 4).toDS()

    //df怎么用，ds就怎么用
    ds.show()


  }

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("df").getOrCreate()

  @After
  def close()={
    sparkSession.stop()
  }

}