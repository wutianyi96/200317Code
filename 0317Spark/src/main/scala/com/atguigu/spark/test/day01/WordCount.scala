package com.atguigu.spark.test.day01

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by VULCAN on 2020/7/10
 *
 *      Hadoop MR WordCount:
 *            ①  Configuration conf = new Configuration()
 *                用来读取加载定义的MR程序的各种配置参数
 *
 *            ②  Job job=Job.getInstance(conf);
 *
 *                  Job作为当前程序的抽象表示！
 *                  通过Job来定义程序需要用到哪些组件
 *                  job.setMapper()
 *                  job.setReducer()
 *
 *            ③ job.waitForCompletion()
 *                  初始化 JobContextImpl() :  Job的上下文实现
 *                  JobContextImpl 所有的内容，传递Mapper的Context,Reducer的Context
 *                      Context:  上下文
 *                          上文：  当前Job是怎么定义的，怎么配置的
 *                          下文：  当前Job要提交到哪个集群，以什么方式运行，下一步改运行什么组件
 *
 *                          Job的环境
 *
 *                 运行模式：   mapred-site.xml  mapreduce.framework.name
 *                            ①local(默认)
 *                            ②YARN
 *
 *       Spark WC :
 *                ①创建一个配置文件对象，读取Spark程序的默认配置和用户自定义的配置
 *                ② 需要有程序的上下文Context对象，通过Context来执行程序
 *
 *                  运行模式：
 * *                            ①local(本地)
 * *                            ②Standalone(独立)
 *                              ③ YARN，MESORS,K8S
 *
 *
 *       高阶函数： 算子。
 *              分类：   转换算子 map,groupby
 *                            是一种懒加载 ,lazy
 *                      行动算子:  调用行动算子，程序才开始运算！
 *
 *
 */
object WordCount {

  def main(args: Array[String]): Unit = {

    /*
        创建一个配置文件对象
            创建对象后，自动读取系统中默认的和Spark相关的参数，用户也可以自定义，覆盖默认参数


            setMaster：  设置程序在哪个集群运行，设置就去哪的Master进程
                local :  本地模式

            setAppName:  类似Job.setName()
                          给程序起名称

              Spark相对于Project作为相对路径的根路径

     */
    val conf = new SparkConf().setMaster("local").setAppName("My app")

    // 用户自定义参数
    //conf.set()

    /*
        SparkContext:  一个应用中最核心的对象！
                        可以连接Spark集群，创建编程需要的RDD,累加器，广播变量

                        RDD: 弹性分布式数据集，简单理解为就是一个集合List
     */
    val sparkContext = new SparkContext(conf)

    // 将文件中的每一行内容读取到集合    Source.fromfile(xx).getLines.toList
    // RDD[String]  类比为List[String]   List("hello hello hi hi","nice to meet you" )
    val rdd: RDD[String] = sparkContext.textFile("input")

    //  List("hello hello hi hi","nice to meet you" ) =>  List("hello","hello",..)
    val words: RDD[String] = rdd.flatMap(x => x.split(" "))

    //  List("hello","hello",..) =>  Map[String,List[String]] {("hello",List("hello"...))   }
    val wordMap: RDD[(String, Iterable[String])] = words.groupBy(x => x)

    // Map[String,List[String]] {("hello",List("hello"...))   } =>  Map[String,Int] {("hello",3)...}
    val wordCount: RDD[(String, Int)] = wordMap.map(x => (x._1, x._2.size))

    // 调用行动算子
    val result: Array[(String, Int)] = wordCount.collect()

    println(result.mkString(","))

    //关闭上下文
    sparkContext.stop()


  }
}
