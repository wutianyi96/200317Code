package com.atguigu.spark.test.day08

/**
  * Created by VULCAN on 2020/7/21
  *
  *  数据的加载和保存
  */

import java.util.Properties

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.junit._

case class  MyUser(name:String,age:Int,gender:String)

class DataInputOutputTest {

  /*
        写
   */
  @Test
  def test7() : Unit ={

    import  sparkSession.implicits._

    val users = List(MyUser("jack1", 20, "male"), MyUser("jack2", 22, "male"), MyUser("jack3", 23, "male"))

    val ds: Dataset[MyUser] = users.toDS()

    ds.write
      .option("url","jdbc:mysql://localhost:3306/0508")
      .option("user","root")
      .option("password","guodai")
      .option("dbtable","MyUser")
      .mode("append")
      .format("jdbc").save()

  }

  /*
        参数可以参考：JDBCOptions
   */
  @Test
  def test6() : Unit ={

    //通用
    val df: DataFrame = sparkSession.read
      .option("url","jdbc:mysql://localhost:3306/0508")
      .option("user","root")
      .option("password","guodai")
      // 查询全表
      //.option("dbtable","tbl_user")
      //  query和dbtable 互斥
      // 自定义查询
      .option("query","select * from tbl_user where id > 5")
      .format("jdbc").load()

    df.show()

  }

  /*
      从关系型数据库中读写数据
            ①配置驱动
   */
  @Test
  def test5() : Unit ={

    val properties = new Properties()

    properties.put("user","root")
    properties.put("password","guodai")

    //专用
    val df2: DataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/0508","tbl_user",properties)

    //全表查询
    df2.show()

    df2.createTempView("user")

    import  sparkSession.sql

    //只查询部分数据
    sql("select * from user where id > 5").show()

  }


  /*
      不标准的csv: 在读取时，需要添加参数
          sep: csv文件中的分隔符
   */
  @Test
  def test4() : Unit ={

    val df2: DataFrame = sparkSession.read
      .option("sep",":")
      .option("header",true)
      .csv("input/mycsv.csv")

    df2.show()

    df2.write
      .option("sep",",")
      .option("header",false)
      .mode("append").csv("output4")

  }

  /*
      CSV:  逗号分割的数据集，每行数据的字段都使用,分割
      tsv:  使用\t分割的数据集
   */
  @Test
  def test3() : Unit ={

    //通用的读取
    val df: DataFrame = sparkSession.read.format("csv").load("input/mycsv.csv")

    // df.show()

    //专用
    val df2: DataFrame = sparkSession.read.csv("input/mycsv.csv")

    df2.show()
    /*
    +----+---+----+
    | _c0|_c1| _c2|
    +----+---+----+
    |jack| 20|male|
    |jack| 20|male|
    |jack| 20|male|
    +----+---+----+
     */


  }

  @Test
  def test2() : Unit ={

    //省略建表的过程，直接指定数据源
    sparkSession.sql("select * from json.`input/employees.json`").show()

  }

  /*
      通用的读取
            在spark中默认支持读取和写出的文件格式是parquet格式！
            Parquet(snappy)
   */
  @Test
  def test1() : Unit ={

    import  sparkSession.implicits._

    //通用方法，可以通过参数改变读取文件的类型
    val df: DataFrame = sparkSession.read.format("json").load("input/employees.json")

    //等价于
    // 专用读取JSON方法
    val df2: DataFrame = sparkSession.read.json("input/employees.json")

    //df.show()

    // df2.show()

    val df3: DataFrame = df2.select('name, 'salary + 1000 as("newSalary"))

    //df3.show()

    //以通用的API，JSON格式写出
    //df3.write.format("json").save("output2")

    //专用方法
    df3.write.mode("append").json("output3")

  }

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("df").getOrCreate()

  @After
  def close()={
    sparkSession.stop()
  }

}
