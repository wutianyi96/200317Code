package com.atguigu.spark.test.day09

/**
  * Created by VULCAN on 2020/7/23
  *
  *
  */
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.junit._

class KafkaDirectMode {

  /*
     def createDirectStream[K, V](
      ssc: StreamingContext,      上下文
      locationStrategy: LocationStrategy,   为Executor中的消费者线程分配主题分区的负责均衡策略，通常都用这个！
      consumerStrategy: ConsumerStrategy[K, V] ： ConsumerStrategies.Subscribe  系统自动为消费者组分配分区！自动维护offset!

    ): InputDStream[ConsumerRecord[K, V]] = {
    val ppc = new DefaultPerPartitionConfig(ssc.sparkContext.getConf)
    createDirectStream[K, V](ssc, locationStrategy, consumerStrategy, ppc)
  }
   */
  @Test
  def test1() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(2))

    /*
          ConsumerConfig

          earliest： 只在消费者组不存在时生效！
     */
    val kafkaParams = Map[String,String](
      "group.id" -> "atguigu",
      "bootstrap.servers" -> "hadoop102:9092",
      "client.id" -> "1",		//客户端ID
      "auto.offset.reset" -> "earliest",	//重置offset,从最开始开始消费
      "auto.commit.interval.ms"->"500",
      "enable.auto.commit"->"true",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )

    //获取Reciever  Subscribe模式自动维护offset    《消费逻辑》
    val ds: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      context,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](List("hello"), kafkaParams))

    // ConsumerRecord[String, String] 只处理value
    val ds1: DStream[String] = ds.map(record => {
      /*if (record.value() == "d" ){
        throw new Exception("异常了")
      }*/
      record.value()
    })

    // 《业务处理逻辑》
    val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print(100)

    context.start()

    context.awaitTermination()

  }




  /*
        解决丢数据：  ①取消自动提交
                     ②在业务逻辑处理完成之后，再手动提交offset

                     spark允许设置checkpoint，在故障时，自动将故障的之前的状态(包含为提交的offset)存储！
                     可以在重启程序后，重建状态，继续处理！
   */
  @Test
  def test2() : Unit ={

    def rebuild() :StreamingContext={
      val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

      val context = new StreamingContext(conf, Seconds(2))

      //设置ck目录
      context.checkpoint("kafka")

      val kafkaParams = Map[String,String](
        "group.id" -> "atguigu",
        "bootstrap.servers" -> "hadoop102:9092",
        "client.id" -> "1",
        "auto.offset.reset" -> "earliest",
        "auto.commit.interval.ms"->"500",
        "enable.auto.commit"->"false",
        "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
        "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
      )

      //获取Reciever  Subscribe模式自动维护offset   《消费逻辑》
      val ds: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](context,
        LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe[String, String](List("hello"), kafkaParams))

      // ConsumerRecord[String, String] 只处理value
      val ds1: DStream[String] = ds.map(record => {
        /* if (record.value() == "d" ){
           throw new Exception("异常了")
         }*/
        record.value()
      })

      // 《业务处理逻辑》
      val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

      result.print(100)

      context

    }

    /*
        获取一个active的sc，或从checkpoint的目录中重建sc，或new
     */
    val context: StreamingContext = StreamingContext.getActiveOrCreate("kafka", rebuild)

    context.start()

    context.awaitTermination()

  }

}
