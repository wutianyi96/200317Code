package com.atguigu.spark.test.day07

/**
  * Created by VULCAN on 2020/7/20
  *
  *    UDF:一进一出
  *    UDAF： 多进一出
  */
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.{Aggregator, MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, Row, SparkSession, TypedColumn}
import org.junit._

/*
    非常类似累加器
 */
//用户定义的聚集函数
class  MySum extends UserDefinedAggregateFunction {

  //输入的数据的结构信息（类型）		 整条字段		字段    名字无关紧要
  override def inputSchema: StructType = StructType( StructField("input",DoubleType)::Nil)

  // buffer: 缓冲区(用于保存途中的临时结果)
  // bufferSchema： 缓存区的类型
  override def bufferSchema: StructType =  StructType( StructField("sum",DoubleType)::Nil)

  // 最终返回的结果类型
  override def dataType: DataType = DoubleType

  //是否是确定性的，传入相同的输入是否会总是返回相同的输出
  override def deterministic: Boolean = true

  //初始化缓冲区，赋值zero value  MutableAggregationBuffer extends Row
  override def initialize(buffer: MutableAggregationBuffer): Unit = buffer(0)=0.0

  //将输入的数据，进行计算，更新buffer  分区内运算
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {

    // input: Row:  输入的这一列
    buffer(0)= buffer.getDouble(0) + input.getDouble(0)

  }

  // 分区间合并结果  将buffer2的结果累加到buffer1
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer2.getDouble(0)+buffer1.getDouble(0)
  }
  // 返回最后的结果的值
  override def evaluate(buffer: Row): Any = buffer(0)
}





class  MyAvg extends  Aggregator[User1,MyBuffer,Double] {

  //初始化缓冲区
  override def zero: MyBuffer = MyBuffer(0.0 , 0)

  // 分区内聚合
  override def reduce(b: MyBuffer, a: User1): MyBuffer = {
    // 累加buffer的sum
    b.sum += a.salary
    // 累加buffer的count
    b.count +=1

    b

  }

  // 分区间聚合，将b2的值聚合到b1上
  override def merge(b1: MyBuffer, b2: MyBuffer): MyBuffer = {
    // 累加buffer的sum
    b1.sum += b2.sum
    // 累加buffer的count
    b1.count += b2.count
    b1
  }

  //返回结果
  override def finish(reduction: MyBuffer): Double = reduction.sum / reduction.count

  // buffer的Encoder类型  样例类，ExpressionEncoder[样例类类型] 或 Encoders.product
  override def bufferEncoder: Encoder[MyBuffer] = ExpressionEncoder[MyBuffer]

  // 最终返回结果的Encoder类型
                          /**
                            * Encoder可以理解为使用ExpressionEncoder()构造编码器的工厂
                            *
                            *       使java等其他语言的类型可以转化为scala适用的类型！！
                            * */
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

// 每个样例类都会实现Product和Serilizable
case class MyBuffer(var sum:Double,var count:Int)

class CustomFunctionTest {

  /*
      Aggregator[IN, BUF, OUT]
          IN: 输入的类型   User1
          BUF： 缓冲区类型   MyBuffer
          OUT：输出的类型  Double

          和DataSet配合使用，使用强类型约束！

        求平均值：    sum /  count

   */

  @Test
  def test7() : Unit ={

    import  sparkSession.implicits._

    val df: DataFrame = sparkSession.read.json("input/employees.json")

    val ds: Dataset[User1] = df.as[User1]

    //创建UDAF
    val myAvg = new MyAvg

    // 将UDAF转换为一个列名
    val aggColumn: TypedColumn[User1, Double] = myAvg.toColumn

    // 使用DSL风格查询
    ds.select(aggColumn).show()

  }

  /*
        UDAF: 老版本，继承UserDefinedAggregateFunction实现
        模拟 sum()

        ①创建函数
        ②注册函数
        ③使用函数
   */
  @Test
  def test2() : Unit ={

    val df: DataFrame = sparkSession.read.json("input/employees.json")

    df.printSchema()
    /*root
     |-- name: string (nullable = true)
     |-- salary: long (nullable = true)
    */

    //创建函数对象
    val mySum = new MySum

    //注册
    sparkSession.udf.register("mySum",mySum)

    //使用
    df.createTempView("emp")

    sparkSession.sql("select mySum(salary) from emp").show()


  }

  /*
      UDF:
            ①定义函数
            ②注册函数
            ③使用

		employees.json:

		{"name":"Michael", "salary":3000}
		{"name":"Andy", "salary":4500}
		{"name":"Justin", "salary":3500}
		{"name":"Berta", "salary":4000}

   */
  @Test
  def test() : Unit ={

    import  sparkSession.implicits._

    // sayHello("jack") ==> "Mr.jack"
    // 定义加注册
    //自定义函数				函数名		匿名函数		值
    sparkSession.udf.register("sayHello",(name:String) => "Mr."+name )

    val df: DataFrame = sparkSession.read.json("input/employees.json")

    val ds: Dataset[User1] = df.as[User1]

    ds.show()
    /*+-------+------+
    |   name|salary|
    +-------+------+
    |Michael|  3000|
    |   Andy|  4500|
    | Justin|  3500|
    |  Berta|  4000|
    +-------+------+
    */

    //自定义的函数需要在sql中使用，需要先创建表哦
    ds.createTempView("emp")

    // 执行sql						name是列，代表传入的是json的name这一列值进来
    sparkSession.sql("select sayHello(name) from emp").show()
    /*+--------------+
    |sayHello(name)|
    +--------------+
    |    Mr.Michael|
    |       Mr.Andy|
    |     Mr.Justin|
    |      Mr.Berta|
    +--------------+
    */


  }

  val sparkSession: SparkSession = SparkSession.builder().master("local[*]").appName("df").getOrCreate()

  @After
  def close()={
    sparkSession.stop()
  }

}
