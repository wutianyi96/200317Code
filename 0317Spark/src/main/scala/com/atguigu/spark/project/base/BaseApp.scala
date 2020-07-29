package com.atguigu.spark.project.base

import com.atguigu.spark.project.app.Top10HotCategoryTop10SessionApp.sc
import com.atguigu.spark.project.bean.UserVisitAction
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by VULCAN on 2020/7/17
  *
  *    完成  创建环境，释放环境，删除输出目录
  */
abstract class BaseApp {

  //应用输出的目录
  val outPutPath : String

  val sc = new SparkContext(new SparkConf()

    .setAppName("My app")
    .setMaster("local[*]")
  )

  //核心： 运行APP
  def runApp( op: =>Unit ) ={

    //清理输出目录
    init()

    //核心运行子类提供的代码
    try {
      op
    } catch {
      case e:Exception => println("出现了异常:"+e.getMessage)
    } finally {
      //关闭连接
      sc.stop()
    }

  }


  def init(): Unit ={

    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path(outPutPath)

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

  }

  def getAllBeans(): RDD[UserVisitAction]={

    val datas: RDD[String] = sc.textFile("datas/user_visit_action.txt")

    val beans: RDD[UserVisitAction] = datas.map(line => {

      val words: Array[String] = line.split("_")

      UserVisitAction(
        words(0),
        words(1).toLong,
        words(2),
        words(3).toLong,
        words(4),
        words(5),
        words(6).toLong,
        words(7).toLong,
        words(8),
        words(9),
        words(10),
        words(11),
        words(12).toLong
      )
    })

    beans

  }


}
