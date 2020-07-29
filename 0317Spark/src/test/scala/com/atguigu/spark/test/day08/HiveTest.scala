package com.atguigu.spark.test.day08

/**
  * Created by VULCAN on 2020/7/21
  *
  */
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.junit._
class HiveTest {

  @Test
  def test2() : Unit ={

    System.setProperty("HADOOP_USER_NAME","atguigu")
    val spark: SparkSession = SparkSession.builder().
      master("local[*]").
      appName("df").
      enableHiveSupport().
      config("spark.sql.warehouse.dir","hdfs://hadoop102:9820/user/hive/warehouse")
      .getOrCreate()

    spark.sql("show tables").show()


    // val df: DataFrame = spark.sql("select * from emp where sal > 2000")

    //  df.show()

    //将df中的数据写出
    // insert overwrite table
    //df.write.mode("overwrite").saveAsTable("emp2")

    // insert into table   写出的数据的列名和表中的字段名一致即可，顺序可以无所谓
    //df.write.mode("append").saveAsTable("emp2")

  }

  /*
      IDEA和外置hive交互
            Hive on Spark:  在hive的配置中，有执行引擎，选择 mr,tez,spark
                              自己解决兼容性

            Spark on Hive:   spark SQL集成hive
            Spark SQL:  完全兼容hive sql
            Hive SQL:  复合hive要求的语法
                        insert into [table] 表名
   */
  @Test
  def test1() : Unit ={

    //System.setProperty("HADOOP_USER_NAME","atguigu")

    //原理： SparkSession在构建时，会读取电脑中的环境变量 Enviroments Variables
    //  读取系统变量 System.setProperty  , 读每个框架的配置文件
    val spark: SparkSession = SparkSession.builder().
      master("local[*]").
      appName("df").
      enableHiveSupport().
      config("spark.sql.warehouse.dir","hdfs://hadoop102:9820/user/hive/warehouse")
      .getOrCreate()

    //spark.sql("show tables").show()

    //spark.sql("create table p1(name string)").show()

    spark.sql("create database mydb12").show()

    // spark.sql("select avg(sal) from emp").show()

    spark.stop()





    /*spark.sql("create table p1(name string)").show()

    spark.sql("show tables").show()
*/

  }


  /*val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("df").enableHiveSupport().getOrCreate()

  @After
  def close()={
    sparkSession.stop()
  }*/



}
