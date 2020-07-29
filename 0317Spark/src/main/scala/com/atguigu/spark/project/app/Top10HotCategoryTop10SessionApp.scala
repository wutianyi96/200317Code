package com.atguigu.spark.project.app

import com.atguigu.spark.project.base.BaseApp
import com.atguigu.spark.project.bean.UserVisitAction
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

/**
  * Created by VULCAN on 2020/7/18
  *
  * Top10热门品类中每个品类的 点击数最多的 Top10Session统计
  *
  *    数据：  一条代表一个session对 商品的搜索或 某个品类的  点击，下单，支付的访问！
  *
  *      ①获取需求一统计的top10 热度的 品类
  *      ②进一步统计，这10个品类中，每个品类，top10的session
  *            a) 先过滤出top10类型的所有数据
  *            b) 再过滤出 top10类别的点击数据
  *            c)  按照   类别id-sessionid   对以上过滤的数据进行分组
  *            d)  统计每个类别下，每个session在数据中一共点击多少次 ：       类别id-sessionid-10
  *            e)  将数据转换   类别id , sessionid-10, 按照类别id 分组      类别id，List(sessionid1-N,sessionid2-N...)
  *            f) 按照次数进行排序，求前十    { (类别id1，List(sessionid1-N,sessionid2-N...) )
  *                                          (类别id2，List(sessionid1-N,sessionid2-N...)  }
  *
  */
object Top10HotCategoryTop10SessionApp extends  BaseApp{
  override val outPutPath: String = "output/Top10HotCategoryTop10SessionApp"



  def main(args: Array[String]): Unit = {

    runApp{

      // 1.获取需求一统计的top10 热度的 品类
      val rdd1: RDD[String] = sc.textFile("output/Top10HotCategoryACC")

      val top10Catagorys: List[String] = rdd1.collect().toList

      // top10热度的类别  只读
      val top10CategoryNames: List[String] = top10Catagorys.map(line => line.split(",")(0))


      //放入广播变量
      val top10CategoryNamesBc: Broadcast[List[String]] = sc.broadcast(top10CategoryNames)

      //a) 先过滤出top10类型的所有数据 ,  b) 再过滤出 top10类别的点击数据
      val beans: RDD[UserVisitAction] = getAllBeans()


      // 在map中过滤，过滤后，复合要求的数据，封装    List[  ((Long, String), Int),((Long, String), Int),((Long, String), Int)           ]
      val rdd2: RDD[((Long, String), Int)] = beans.flatMap(bean => {

        if (bean.click_category_id != -1 && top10CategoryNamesBc.value.contains(bean.click_category_id+"")) {

          List(((bean.click_category_id, bean.session_id), 1))

        } else {

          Nil

        }

      })

      //rdd2.collect().foreach(println)


      // 根据(品类ID, sessionID) 聚合，统计每个品类下每个session点击了多少次
      val rdd3: RDD[((Long, String), Int)] = rdd2.reduceByKey(_ + _)


      // 完成格式的转换   ((品类ID, sessionID),N)  =>  (品类ID ( sessionID,N))
      val rdd4: RDD[(Long, (String, Int))] = rdd3.map {

        case ((categoryName, sessionId), count) => (categoryName, (sessionId, count))

      }

      // 分组，将品类ID相同的分组
      val rdd5: RDD[(Long, Iterable[(String, Int)])] = rdd4.groupByKey(1)

      //排序取前10
      val rdd6: RDD[(Long, List[(String, Int)])] = rdd5.mapValues(it => {

        //降序排序
        it.toList.sortBy(x => -x._2).take(10)

      })

      rdd6.saveAsTextFile(outPutPath)



    }
  }
}
