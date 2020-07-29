package com.atguigu.spark.test.day09

/**
  * Created by VULCAN on 2020/7/23
  *
  *    离线(不在线)数据：   不是此时此刻正在生成的数据！明确的生命周期！
  *    实时数据：   此时此刻正在生成的数据！只有开始没有结束！ 源源不断的流式数据！
  *
  *    离线计算：   计算需要花费一定的周期，才能生成结果！不能立刻，现在就出结果！
  *    实时计算：    立刻，现在就出结果！
  *
  *    Spark Streaming：  准实时！ 接近实时！  微批次处理！  本质上还是批处理！每次处理的一批数据量不是很大！
  *              流式数据！ 数据是不是源源不断！
  *
  *     基本的数据抽象： 定义数据源，定义数据的处理逻辑
  *        SparkCore :  RDD
  *        SparkSql  :  DataFrame,DataSet
  *                        对RDD的封装！
  *        SparkStreaming:  DStream(离散化流)，流式数据可以离散分布到多个Excutor进行并行计算！
  *                                本质还是对RDD的封装
  *
  *
  *      数据的语义：
  *          at most once :  至多一次  0次或1次，会丢数据！
  *          at least once： 至少一次。 不会丢数据！  有可能会重复！
  *          exactly once： 精准一次
  *
  *         xxx 框架，支持 at least once
  *
  *
  *      整体架构：  ①SparkStreaming一开始运行，就需要有Driver，还必须申请一个Executor，运行一个不会停止的task!
  *                      这个task负责运行 reciever，不断接受数据
  *
  *                 ②满足一个批次数据后，向Driver汇报，生成Job，提交运行Job，由 reciever将这批数据的副本发送到 Job运行的Exceutor
  *
  *
  *
  *    有StreamingContext作为应用程序上下文：
  *            只能使用辅助构造器构造！
  *            可以基于masterurl和appname构建，或基于sparkconf构建，或基于一个SparkContext构建！
  *
  *            获取StreamingContext中关联的SparkContext： StreamingContext.SparkContext
  *
  *            得到或转换了Dstream后，Dstream的计算逻辑，会在StreamingContext.start()后执行，在StreamingContext.stop()后结束！
  * *
  *            StreamingContext.awaitTermination()： 阻塞当前线程，直到出现了异常或StreamingContext.stop()！
  *
  *            作用： 创建Dstream(最基本的数据抽象模型)
  */
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.junit._

import scala.collection.mutable
class HelloWorldTest {

  /*
        创建一个Quene[RDD],让接收器每次接受队列中的一个RDD进行运算！

    def queueStream[T: ClassTag](
      queue: Queue[RDD[T]],    读取数据的队列
      oneAtATime: Boolean,   每个周期是否仅从 队列中读取一个RDD
      defaultRDD: RDD[T]    如果队列中的RDD被消费完了，返回一个默认的RDD，通常为null
    ): InputDStream[T] = {
    new QueueInputDStream(this, queue, oneAtATime, defaultRDD)
  }
   */
  @Test
  def test2() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(2)) //每两秒窗口接收一次

    //创建一个可变的Quene
    val queue: mutable.Queue[RDD[String]] = mutable.Queue[RDD[String]]()

    //是否一次取一个
    val ds: InputDStream[String] = context.queueStream(queue, false, null)

    val result: DStream[(String, Int)] = ds.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print(100)

    context.start()

    val rdd: RDD[String] = context.sparkContext.makeRDD(List("hello hi hello hi"), 1) //分区数


    //向Quene中放入RDD
    for (i <- 1 to 100){  //从1循环到100
      Thread.sleep(1000) //每隔一秒向queue放一个
      queue.enqueue(rdd)
    }

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

  /*
       使用netcat 绑定一个指定的端口，让SparkStreaming程序，读取端口指定的输出信息！
   */
  @Test
  def test1() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(5))

    //获取数据抽象 Dstream  默认以\n作为一条数据，每行数据为一条
    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //执行各种转换 单词统计     进来的都是RDD，所以能使用算子
    val ds2: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    //输出每个窗口计算后的结果的RDD的前100条数据！
    ds2.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

}
