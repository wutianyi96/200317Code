package com.atguigu.spark.test.day06

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/17
  */
class ReadAndWriteOutsideTEST {

  /*
      写HBase
           输出格式： TableOutPutFormat: 将K-V写入到hbase表中
                                 RecordWriter: [KEY, Mutation]
                                               [ImmutableBytesWritable  ,Put]
   */
  @Test
  def test10() : Unit ={

    val conf: Configuration = HBaseConfiguration.create()
    //写入哪个表
    conf.set(TableOutputFormat.OUTPUT_TABLE,"t2")

    // 设置让当前的Job使用TableOutPutFormat，指定输出的key-value类型
    val job: Job = Job.getInstance(conf)

    // 设置输出格式
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

    //指定输出的key-value类型
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    //准备数据
    val list = List(("r3", "cf1", "age", "20"), ("r3", "cf1", "name", "marray1"), ("r4", "cf1", "age", "20"), ("r4", "cf1", "name", "tony"))

    val rdd: RDD[(String, String, String, String)] = sc.makeRDD(list, 2)

    //在rdd中封装写出的数据
    val datas: RDD[(ImmutableBytesWritable, Put)] = rdd.map {

      case (rowkey, cf, cq, v) => {

        val key = new ImmutableBytesWritable()
        key.set(Bytes.toBytes(rowkey))

        val value = new Put(Bytes.toBytes(rowkey))
        value.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cq), Bytes.toBytes(v))

        (key, value)
      }

    }

    //datas写入到hbase
    datas.saveAsNewAPIHadoopDataset(job.getConfiguration)

  }

  /*
       读HBase
                  输入格式： TableInputFormat: 读取一个表中的数据，封装为K-V对
                                  RecordReader:[ImmutableBytesWritable, Result]
                                      ImmutableBytesWritable: rowkey
                                      Result: 当前行的内容


   */
  @Test
  def test9() : Unit ={

    // 需要有和HBase相关的配置对象，通过配置对象获取hbase所在集群的地址
    val conf: Configuration = HBaseConfiguration.create()

    // 指定要用TableInputFormat读取哪个表
    conf.set(TableInputFormat.INPUT_TABLE,"t2")

    // 读HBase，返回RDD
    //   通过指定的InputFormat，从Hadoop文件中获取数据
    val rdd: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[ImmutableBytesWritable], classOf[Result])

    val resultRDD: RDD[String] = rdd.flatMap {
      case (rowKey, result) => {

        val cells: Array[Cell] = result.rawCells()

        for (cell <- cells) yield {

          val rk: String = Bytes.toString(CellUtil.cloneRow(cell))
          val cf: String = Bytes.toString(CellUtil.cloneFamily(cell))
          val cq: String = Bytes.toString(CellUtil.cloneQualifier(cell))
          val value: String = Bytes.toString(CellUtil.cloneValue(cell))

          //返回的内容
          rk + ":" + cf + ":" + cq + "=" + value

        }
      }
    }

    resultRDD.collect().foreach(println)

  }

  /*
      写数据库
   */
  @Test
  def test8() : Unit ={

    val list = List(("marray", "18xaefa"), ("marry1", "20"),("marray2", "18"), ("marry2", "20"))

    val rdd: RDD[(String, String)] = sc.makeRDD(list, 2)

    //向数据库中写
    rdd.foreachPartition( it=>{

      //注册驱动
      Class.forName("com.mysql.jdbc.Driver")
      //获取连接
      val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/0508", "root", "guodai")
      //声明准备
      val ps: PreparedStatement = connection.prepareStatement("insert  into tbl_user(username,password) values(?,?)")

      it.foreach{

        case (name,age) => {
          ps.setString(1,name)
          ps.setString(2,age)
          //执行
          ps.execute()
        }

      }
      ps.close()
      connection.close()
    } )





  }

  /*
      读数据库
   */
  @Test
  def test7() : Unit ={

    //创建RDD，RDD读取数据库中的数据
    val rdd = new JdbcRDD(sc, () => {
      //注册驱动
      Class.forName("com.mysql.jdbc.Driver")
      //获取连接
      val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/0508", "root", "guodai")
      connection
    }, "select * from tbl_user where id >= ? and id < ?", 1, 3, 1,  //lowerBound,upperBound,numPartitions
      //调用ResultSet方法，可以返回一个结果集
      (rs: ResultSet) => {
        rs.getInt("id") + "---" + rs.getString("username") + "---" + rs.getString("password")
      })

    rdd.collect().foreach(println)


  }

  /*
        读SequnceFile
   */
  @Test
  def test6() : Unit ={

    val rdd: RDD[(Int, String)] = sc.sequenceFile[Int, String]("seq")

    rdd.foreach(println)

  }

  /*
        写SequnceFile
   */
  @Test
  def test5() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 1)

    val rdd2: RDD[(Int, String)] = rdd.map((_, "hello"))

    //写
    rdd2.saveAsSequenceFile("seq")


  }

  /*
     读对象文件
  */
  @Test
  def test3() : Unit ={

    val rdd: RDD[Int] = sc.objectFile[Int]("obj")

    rdd.foreach(println)

  }

  /*
      写对象文件
   */
  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 1)

    //写
    rdd.saveAsObjectFile("obj")

  }

  /*
      读写文本文件
   */
  @Test
  def test() : Unit ={

    //  new HadoopRDD 从文本文件读
    sc.textFile("input")

    // rdd.saveTextAsTextFile("")

  }

  val sc = new SparkContext(new SparkConf()

    // 在scala中获取一个类想Class类型   ：  classof[类型]
    .setAppName("My app")
    .setMaster("local[*]")

  )

  // 提供初始化方法，完成输出目录的清理
  // 在每个Test方法之前先运行
  @Before
  def init(): Unit ={

    val fileSystem: FileSystem = FileSystem.get(new Configuration())

    val path = new Path("output")

    // 如果输出目录存在，就删除
    if (fileSystem.exists(path)){
      fileSystem.delete(path,true)
    }

  }

  // 每次测试完成后运行
  @After
  def stop(): Unit ={
    sc.stop()
  }

}
