package com.atguigu.spark.test.day09

import java.io.{BufferedReader, InputStreamReader}
import java.net.{ConnectException, Socket}
import java.nio.charset.Charset

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver

import scala.collection.mutable

/**
  * Created by VULCAN on 2020/7/23
  *
  *    自定义Receiver:  ① 定义onStart() :   完成开始接受数据前的准备工作！
  *                     ② 定义onStop() :   完成停止接受数据前的清理工作！
  *                     ③异常时，可以调用restart()，重启接收器或调用stop()，彻底停止！
  *                     ④调用store()存储收到的数据
  *
  *                     注意： onstart()不能阻塞，收数据需要在新线程中接受（源源不断接收数据会造成阻塞）！
  *                            异常时，在任意线程都可以调用停止，重启，报错等方法！
  */
import  org.junit._
class CustomReciever {

  @Test
  def test1() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(2))

    // 使用自定义的接收器
    val ds1: ReceiverInputDStream[String] = context.receiverStream(new MyReciver("hadoop103", 3333))

    val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print(100)

    context.start()

    context.awaitTermination()

  }

}

/*
      模拟HelloWorld中，通过接受一个端口发送的源源不断的信息
 */				        //为了创建Socket									       //接收值的类型 	 //这是主构造 重写抽象属性
class MyReciver(val host:String,val port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {

  var socket:Socket = null
  var reader:BufferedReader=null

  /*
      收数据之前的准备工作
            创建一个连接此端口的Socket

      收数据时：
            通过Socket获取输入流

   */
  override def onStart(): Unit = {

    try {
      socket = new Socket(host, port)
    } catch {
      case e:ConnectException => restart("重新连接试试")
        return
    }

    println("已经连接上！")

    //收数据前的准备工作最好放在onStart
    //获取socket中的输入流，一行一行读
    reader= new BufferedReader(new InputStreamReader(socket.getInputStream, Charset.forName("utf-8")))

    //新线程中接收数据
    recieveData()
  }

  /*
      在停止收数据之前，清理之前安装的东西(比如Socket对象)
   */
  override def onStop(): Unit = {

    //如果socket正在连接 就关闭socket
    if (socket != null){
      socket.close()
      socket=null
    }

    if (reader !=null){
      reader.close()
      reader=null
    }

  }

  def recieveData()={

    new Thread(){

      //设置当前线程为守护线程
      // 如果一个JVM中只有守护线程，虚拟机就关闭！
      setDaemon(true)

      //线程运行，完成数据的接受
      override def run(): Unit = {

        try {
          var line = reader.readLine()

          while (socket.isConnected && line != null) {

            //存储
            store(line)
            //继续接收
            line = reader.readLine()
          }
        } catch {
          // 再开一个线程收数据
          case e:Exception =>
        } finally {
          //清理
          onStop()
          restart("重新启动！")
        }
      }
    }.start()

  }

}
