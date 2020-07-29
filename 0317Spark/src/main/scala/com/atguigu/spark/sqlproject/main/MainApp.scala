package com.atguigu.spark.sqlproject.main

import org.apache.spark.sql.SparkSession

/**
地区	商品名称	点击次数	城市备注
华北	商品A	100000	北京21.2%，天津13.2%，其他65.6%
    思路：①三表关联，取出需要的字段
        city_info：  area  ， city_id ,city_name
        product_info：product_name ，product_id
        user_visit_action：click_product_id  ，city_id

        ②计算每个商品在每个区域的点击次数
            以区域，商品进行分组

        ③以区域为范围，将每个商品的点击次数进行降序排名，计算每个区域内商品的排名
         ④取前3

*/
object MainApp {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder().
      master("local[*]").
      appName("df").
      enableHiveSupport().
      config("spark.sql.warehouse.dir","hdfs://hadoop102:9820/user/hive/warehouse")
      .getOrCreate()

    import  spark.implicits._

    //三表连接
    val sql1=
      """
        |
        |select
        |   ci.*,
        |   p.product_name,
        |   uv.click_product_id
        | from  user_visit_action uv join  city_info ci on uv.city_id=ci.city_id
        | join product_info p on uv.click_product_id=p.product_id
        |""".stripMargin

    //注册
    val myUDAF = new MyUDAF

    spark.udf.register("myudaf",myUDAF)

    val sql2=
      """
        | select
        |     area,product_name,count(*) clickCount, myudaf(city_name) result
        |  from t1
        |  group by area,click_product_id,product_name
        |""".stripMargin

    val sql3=
      """
        |
        |select  area,product_name,clickCount,result, rank() over(partition by area order by clickCount desc) rn
        |  from t2
        |
        |""".stripMargin

    val sql4=
      """
        | select
        |    area,product_name,clickCount,result
        |  from t3
        |  where rn <=3
        |
        |""".stripMargin

    //切库

    //spark.sql("select * from emp").show()
    spark.sql("use sparksql")



    spark.sql(sql1).createTempView("t1")
    spark.sql(sql2).createTempView("t2")
    spark.sql(sql3).createTempView("t3")
    spark.sql(sql4).show(false)

    spark.close()


  }

}
