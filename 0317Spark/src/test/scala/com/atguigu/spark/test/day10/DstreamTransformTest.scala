package com.atguigu.spark.test.day10

/**
  * Created by VULCAN on 2020/7/24
  *
  *    Dsstream的转换分为无状态和有状态！
  *
  *          状态(state) : 是否可以保留之前批次处理的结果！
  *          无状态： 不会保留之前批次处理的结果！ 每个批次都是独立的，是割裂的！
  *          有状态：  会保留之前批次处理的结果，新的批次在处理时，可以基于之前批次的结果，进行组合运算！
  */
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.junit._
class DstreamTransformTest {

  /*
        Dstream => transform
		=> RDD/DF/DS
			RDD.map
											=>两者区别:
         Dstream.map(本身也有map方法)

   */
  @Test
  def test10() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(3))

    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //在excutor执行
    val ds2: DStream[String] = ds1.map(x => {
      println(Thread.currentThread().getName + ": ds1.map")
      x
    })

    //TODO 调用transfrom时：除了算子在Excutor执行，其他都会在Driver，做周期性的执行 ！！
    val ds3: DStream[String] = ds1.transform(rdd => {

      //JobGenerator步骤  在Driver执行  随着采集周期，每批都会执行一次
      println(Thread.currentThread().getName + ": ds1.transform")

      //在excutor执行
      rdd.map(x => {
        println(Thread.currentThread().getName + ": rdd.map")  //只执行一次！
        x		//x是在最后一行的代码，返回值给DStream就是它
      })

      //这里没有代码，看上一层。
    })

    //ds3.print(100)
    ds2.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

  /*
      如何优雅地关闭流式程序
            context.stop()
   */
  @Test
  def test9() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(3))


    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)


    val result: DStream[(String, Int)] = ds1.window(Seconds(6)).
      flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    result.print(100)
    //启动运算
    context.start()

    //关闭  not ok

    //《只阻塞当前线程》，直到终止
    context.awaitTermination()

    //关闭  需要在一个新的线程中关闭流，因为当前线程已经被阻塞了
    new Thread(){
      setDaemon(true)  //setDaemon直接守护线程，main死了就不用关context了

      override def run(): Unit = {

        //在需要关闭的时候才关闭
        // true = 程序代码，用代码去判断当前是否需要关闭
        //  程序可以尝试去读取一个数据库，或一个指定路径的标识符（如查看一个节点是否存在
        while(!true){ 	//循环执行判断逻辑，看是否需要关
          Thread.sleep(5000)
        }

        // 优雅地关闭，等待最后一批(流式有很多批)已经接收的(不一定需要结果的)数据计算完后 再关闭， （比较绅士
        //是否关  是否优雅地关
        context.stop(true,true)
      }

    }.start()


  }

  /*
        《也可以先定义好窗口，之后的运算都在窗口中计算的方法》   也是比较好的方法
                              一般每次结果保存到数据库中
        需求2：每间隔1min统计数据,  然后统计1h内所有的数据！

   */
  @Test
  def test8() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(3))


    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //《先定义好窗口，之后的运算都在窗口中计算》
    val result: DStream[(String, Int)] = ds1.window(Seconds(6)).
      flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    //result.print(100)


    // 《DStream输出》
    //  	《一般将结果保存到数据库！》
    // 前缀和后缀是输出目录的前缀和后缀       前缀+周期的时间戳+后缀
    // result.repartition(1).saveAsTextFiles("a","out")

    //《foreachRDD的使用》
    // 		foreachPartition 一个分区（文件）调用函数处理一次
    result.foreachRDD(rdd => rdd.foreachPartition(it =>{

      // 新建数据库连接
      // 准备sql
      // 写出数据

    }))

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

  /*
        (策略优化后的例子)
        需求2：每间隔1min统计数据,  然后统计1h内所有的数据！

      def reduceByKeyAndWindow(
      reduceFunc: (V, V) => V,		计算的函数
      invReduceFunc: (V, V) => V, (反向归约)  ._1V: 之前已经运算的结果， ._2V是要离开窗口范围的value
      windowDuration: Duration,		窗口的周期
      slideDuration: Duration = self.slideDuration,		有默认值
      numPartitions: Int = ssc.sc.defaultParallelism,	有默认值
      filterFunc: ((K, V)) => Boolean = null	 过滤函数，感兴趣的留下来，不要的舍弃掉
    ): DStream[(K, V)]

   */
  @Test
  def test6() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(3))

    context.checkpoint("ck")

    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //    reduceByKey: 是无状态计算
    // 而 updateStateByKey : 是有状态计算
    val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1))

      // .reduceByKeyAndWindow(_ + _, windowDuration=Seconds(6), slideDuration=Seconds(3))

      // 如果窗口有重复计算，效率低，可以《优化》:
      //invReduceFunc: (V, V) => V:  ._1V: 之前已经运算的结果， ._2V是要离开窗口范围的value
      .reduceByKeyAndWindow(_ + _, _-_ , windowDuration=Seconds(6),filterFunc=_._2 !=0)
    //把value=0的过滤掉！（因为之前有a的值，被抛弃后没有了，后面会出现(a,0)）

    result.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

  /*

        需求2：每间隔1min统计数据,  然后统计1h内所有的数据！

         def reduceByKeyAndWindow(
      reduceFunc: (V, V) => V,      计算的函数
      windowDuration: Duration,     窗口的周期
      slideDuration: Duration        滑动的步长周期
    ): DStream[(K, V)]

    reduceByKeyAndWindow: 在一个滑动的窗口内，计算一次！

        滑动步长和窗口的范围必须是采集周期的整倍数！
    reduceByKey：  一个采集周期（批次）计算一次！



    采集周期也可以看成是，计算的窗口大小和采集周期一致，且滑动的步长和采集周期一致！

   */
  @Test
  def test5() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(3))

    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //    reduceByKey: 是无状态计算
    // 而 updateStateByKey : 是有状态计算
    val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).
      //窗口的周期				//滑动的步长周期
      reduceByKeyAndWindow(_ + _, windowDuration=Seconds(6), slideDuration=Seconds(3))
    //  reduceByKeyAndWindow(_ + _, windowDuration=Seconds(3)) 默认就用采集周期作为滑动步长


    result.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()


  }

  /*


        需求1： 统计从此刻开始，数据的累计状态！  不断采集周期计算的结果和下一个周期进行累加运算！
            UpdateStateByKey

    def updateStateByKey[S: ClassTag](       // 数据是k-v类型

      updateFunc: (Seq[V], Option[S])  =>  Option[S] ：some有数据 none没数据
									{
										updateFunc会被当前采集周期中的每个key都进行调用，调用后将
                                        当前key的若干value和之前采集周期，key最终的stage进行合并，合并后更新最新状态

                                        Seq[V]： 最新采集周期中多个Key对应的values

                                         Option[S]: 之前采集周期中的Key的状态[有或者没有]}
    ): DStream[(K, S)]


   《 有状态的计算，由于需要checkpoint保存之前周期计算的状态，会造成小文件过多！
          解决方法： ①自己通过程序将过期的小文件删除
                ②不使用checkpoint机制，而是自己持久化状态到Mysql数据库中 》
   */
  @Test
  def test() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(5))

    //checkpoint为了保存之前批次运算的状态
    context.checkpoint("updateState")

    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    //    reduceByKey: 是无状态计算
    // 而 updateStateByKey : 是有状态计算
    val result: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).
      //拿some构建Option
      updateStateByKey((seq: Seq[Int], opt: Option[Int]) =>
      Some(seq.sum + opt.getOrElse(0)))

    result.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，直到终止
    context.awaitTermination()


  }

  /*
      Join:  双流Join（join的是流！）
                  两种实时计算逻辑需求的场景！

             流越多，需要的计算资源就越多！

             采集周期必须是一致的！
   */
  @Test
  def test3() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    // 一个应用里面只能有一个StreamingContext，一个应用中，采集周期是统一的！
    val context1 = new StreamingContext(conf, Seconds(5))
    //val context2 = new StreamingContext(conf, Seconds(3))

    //获取数据抽象 Dstream  默认以\n作为一条数据，每行数据为一条
    val ds1: ReceiverInputDStream[String] = context1.socketTextStream("hadoop103", 3333)
    val ds2: ReceiverInputDStream[String] = context1.socketTextStream("hadoop103", 2222)

    // 《流中的内容必须是k-v类型》
    val ds3: DStream[(String, Int)] = ds1.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)
    val ds4: DStream[(String, Int)] = ds2.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)

    val ds5: DStream[(String, (Int, Int))] = ds3.join(ds4)

    //输出每个窗口计算后的结果的RDD的前100条数据！
    ds5.print(100)

    //启动运算
    context1.start()
    //context2.start()

    //阻塞当前线程，知道终止
    context1.awaitTermination()
    // context2.awaitTermination()
  }

  /*
        不用Dstream的算子，而是转为DS或DF（需要SparkSession）
            Dstream  =>  RDD  =>  DS /DF
   */
  @Test
  def test2() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(5))

    //sparkSession
    val sparkSession: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    import  sparkSession.implicits._

    //获取数据抽象 Dstream  默认以\n作为一条数据，每行数据为一条
    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    // 用DF或DS处理数据
    val dsStream: DStream[Row] = ds1.transform(rdd => {

      val df: DataFrame = rdd.toDF()

      df.createOrReplaceTempView("a")

      val df1: DataFrame = sparkSession.sql("select * from a")
      df1.rdd

    })

    val result: DStream[String] = dsStream.map(row => row.getString(0))

    //输出每个窗口计算后的结果的RDD的前100条数据！
    result.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()

  }

  /*
        transform :

		将一个Dstream，利用函数，将这个Dstream中的所有的RDD，
		转换为其他的RDD（将Dstream的算子转换为RDD，就可以调用RDD的算子进行计算了！），
		之后再返回新的Dstream

   */
  @Test
  def test1() : Unit ={

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    val context = new StreamingContext(conf, Seconds(5))

    //获取数据抽象 Dstream  默认以\n作为一条数据，每行数据为一条
    val ds1: ReceiverInputDStream[String] = context.socketTextStream("hadoop103", 3333)

    // 将之前的 Dstream[String]  => transform  => Dstream[(String,Int)]
    val dsStream: DStream[(String, Int)] = ds1.transform(rdd => {

      val rdd2: RDD[(String, Int)] = rdd.flatMap(line => line.split(" ")).map((_, 1)).reduceByKey(_ + _)
      rdd2

    })

    //输出每个窗口计算后的结果的RDD的前100条数据！
    dsStream.print(100)

    //启动运算
    context.start()

    //阻塞当前线程，知道终止
    context.awaitTermination()

  }

}
