package com.atguigu.spark.test.day07

/**
  * Created by VULCAN on 2020/7/20
  */
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.junit._

case  class  Person(name:String,salary:Double)
class DataFrameTest {

  /*
       直接创建
   */
  @Test
  def test7() : Unit ={

    /*
        def createDataFrame(rowRDD: RDD[Row], schema: StructType)

            rowRDD: 要转换的RDD的类型，必须是RDD[Row]
            schema: 结构！ 在schema中指定一行数据Row对象，含有几个字段，以及他们的类型

                    StructType的创建：  StructType(List[StuctField]

           StructField: 结构中的一个字段

           case class StructField(
                name: String,   必传
                dataType: DataType,  必传
                nullable: Boolean = true,
                metadata: Metadata = Metadata.empty)
     */

    val list = List(("jack1", 20), ("jack2", 20), ("jack3", 20), ("jack4", 20))

    val rdd1: RDD[(String, Int)] = sparkSession.sparkContext.makeRDD(list, 2)

    val rdd2: RDD[Row] = rdd1.map {

      case (name, age) => Row(name, age)

    }

    //val structType: StructType = StructType(List(StructField("username", StringType), StructField("age", IntegerType)))
    val structType: StructType = StructType(StructField("username", StringType) :: StructField("age", IntegerType) :: Nil)


    val df: DataFrame = sparkSession.createDataFrame(rdd2, structType)

    df.show()

  }

  /*
        使用样例类由RDD转DF
   */
  @Test
  def test6() : Unit ={

    val list = List(Person("jack", 2222.22), Person("jack1", 2222.22), Person("jack2", 2222.22), Person("jack3", 2222.22))

    val rdd: RDD[Person] = sparkSession.sparkContext.makeRDD(list, 2)

    import  sparkSession.implicits._

    rdd.toDF("PersonName","PersonSalary").show()

  }



  /*
        RDD转DF

         RDD出现的早，DF出现的晚，RDD在编写源码时，不会提供对DF转换！

         会后出现的DF，又希望RDD可以对DF提供转换，需要扩展RDD类的功能！

            为一个类提供扩展：  ① 动态混入（非侵入式）
                              ② 隐式转换  (非侵入式)   spark使用

           ①在SparkSession.class类中，声明了一个object:implicits。每创建一个SparkSession的对象，这个对象中，就包含
           一个名为implicits的对象.

           ②implicits extends SQLImplicits，在SQLImplicits，提供rddToDatasetHolder，可以将一个RDD转为DatasetHolder

           ③DatasetHolder提供toDS(), toDF()可以返回DS或DF

           结论： 导入当前sparksession对象中的implicits对象中的所有的方法即可！
                    import  sparkSession对象名.implicits._

                    RDD.toDF()----->DatasetHolder.toDF()



           object implicits extends SQLImplicits with Serializable {
    protected override def _sqlContext: SQLContext = SparkSession.this.sqlContext
  }

  implicit def rddToDatasetHolder[T : Encoder](rdd: RDD[T]): DatasetHolder[T] = {
    DatasetHolder(_sqlContext.createDataset(rdd))
  }
   */


  @Test
  def test4() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sparkSession.sparkContext.makeRDD(list, 2)

    //导入隐式转换的函数
    import  sparkSession.implicits._

    // 默认列名为value
    // val df: DataFrame = rdd.toDF

    // 手动指定结构名
    val df: DataFrame = rdd.toDF("num")

    df.show()

  }


  /*
      创建DF，DF转RDD, DF对RDD的封装，RDD就是DF的一个属性！
            DF.rdd()

      type DataFrame = Dataset[Row]
   */
  @Test
  def test3() : Unit ={

    val df: DataFrame = sparkSession.read.json("input/employees.json")

    df.show()

    // df转 RDD
    /*
        Row: 输出的一行！可以看作是个一行内容(多个字段)的集合

        可以使用 Row(xxx,xx,xx)构建一个Row
        可以使用Row(index)访问其中的元素
     */
    val rdd: RDD[Row] = df.rdd

    //val rdd1: RDD[(Any, Any)] = rdd.map(row => (row(0), row(1)))

    //val rdd2: RDD[(String, Long)] = rdd.map(row => (row.getString(0), row.getLong(1)))

    val rdd3: RDD[(String, Long)] = rdd.map {

      case a: Row => (a.getString(0), a.getLong(1))

    }

    rdd3.collect().foreach(println)

  }

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("df").getOrCreate()



  @After
  def close()={
    sparkSession.stop()
  }



  /*
      RDD 转 DataFrame

        提供了SparkSession
   */
  @Test
  def test1() : Unit ={

    // 之前设置master
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("df")

    // ①调用SparkSession.builder().getOrCreate()
    val session: SparkSession = SparkSession.builder().config(conf).getOrCreate()


    //关闭session
    session.stop()

  }

  @Test
  def test2() : Unit ={

    // ①调用SparkSession.builder().getOrCreate()
    val session: SparkSession = SparkSession.builder().master("local[*]").appName("df").getOrCreate()

    //关闭session
    session.stop()

  }


}
