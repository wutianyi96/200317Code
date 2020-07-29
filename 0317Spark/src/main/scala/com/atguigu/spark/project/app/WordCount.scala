package com.atguigu.spark.project.app

import com.atguigu.spark.project.base.BaseApp

/**
  * Created by VULCAN on 2020/7/17
  */
object WordCount extends  BaseApp{

  override val outPutPath: String = "output/wordcount"

  def main(args: Array[String]): Unit = {

    runApp{

      sc.makeRDD(List(1,2,3,4),2).saveAsTextFile(outPutPath)


    }
  }

}
