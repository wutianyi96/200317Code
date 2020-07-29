package com.atguigu.spark.test.day05

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/15
  *
  * 1.闭包检测
  * 无闭包
  * 闭包检测
  * 解决
  * 2.属性序列化
  * 解决
  * 3.函数序列化
  * 解决
  *
  * 4.Kryo:    高效的Java的序列化框架！
  *
  *        java.io.Serializable：  保留除了列的属性值之外的一些其他信息，例如类的继承关系等等！
  *                  在大数据处理领域，不关心继承关系，只关心对象保存的值！
  *
  *          Kryo 节省 10倍网络传输！
  *
  *          使用：  ①要求类实现java.io.Serializable，闭包检查只检查是否实现了java.io.Serializable
  *                  ②使用kryo方式，序列化想序列化的类
  *
  */

case class  User4(age: Int=18,name:String ="jackfaefafafawfe"){}

class User(val age: Int=18) extends  Serializable {}

// 已经实现了序列化
case class User1(age:Int=18)

// 和属性相关
class User2(age:Int=18) {
  def matchAge(data:RDD[Int]) :RDD[Int]={
    // 局部变量和类的成员没有关系，age不构成闭包
    // 在Driver,myAge仍然构成闭包，但是Int已经实现了序列化
    val myAge=age
    // 在Executor
    data.filter( x => x<myAge )
  }
}

// 和方法相关
class User3(age:Int=18) {

  //成员
  def fun(x:Int)={
    x<18
  }
  def matchAge(data:RDD[Int]) :RDD[Int]={

    // 在Executor
    data.filter( x=>x<18 )
  }
}


class SerilizableTest {

  /*
        测试：  java ：java.io.Serializable   504.0 B
                      kyro  :  148.0 B
   */
  @Test
  def test5() : Unit ={

    val list = List(User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4(), User4())

    val rdd: RDD[User4] = sc.makeRDD(list, 2)

    val rdd1: RDD[(Int, User4)] = rdd.map(x => (1, x))

    val rdd2: RDD[(Int, Iterable[User4])] = rdd1.groupByKey()

    rdd2.collect()

    // 暂停进程
    Thread.sleep(10000000)

  }

  /*
        涉及到类的方法的闭包，解决：
              ①类序列化
              ②使用匿名函数代替成员方法
   */
  @Test
  def test4() : Unit ={

    val list = List(18, 2, 3, 14)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val user = new User3()

    // 调用matchAge间接调用了filter，使用user.fun(成员)，构成闭包
    user.matchAge(rdd).collect().foreach(println)

  }

  /*
          一旦某个算子使用到了外部对象的属性，此时也构成闭包，要求对象的类也必须可以序列化！

              解决： ①类 extends  Serializable
   */
  @Test
  def test3() : Unit ={

    val list = List(18, 2, 3, 14)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val user = new User2()

    // 调用matchAge间接调用了filter，使用user.age，构成闭包
    user.matchAge(rdd).collect().foreach(println)

  }

  /*
     有闭包： 如果算子存在闭包，那么在执行前，需要进行闭包检查，如果发现闭包中有
              不可序列化的变量，此时抛异常。

              一旦有异常，此时Job不会提交运行！

      解决： ①闭包变量序列化 extends  Serializable
             ②使用case class
  */
  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    // 在test2()方法中创建
    val user = new User1()

    rdd.foreach( x =>{

      //构成闭包
      println(x+ "------->"+user.age)
    })


  }

  /*
      无闭包
   */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    rdd.foreach( x =>{
      val user = new User()
      println(x+ "------->"+user.age)
    })


  }

  val sc = new SparkContext(new SparkConf()

    // 在scala中获取一个类想Class类型   ：  classof[类型]
    .setAppName("My app")
    .setMaster("local[*]")
    .registerKryoClasses(Array(classOf[User4]))
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
