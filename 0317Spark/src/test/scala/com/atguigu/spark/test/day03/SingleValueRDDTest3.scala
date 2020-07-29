package com.atguigu.spark.test.day03

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.junit._

/**
  * Created by VULCAN on 2020/7/13
  *
  *
  *
  *        抽样查询： 先分桶
  *
  * distinct
  * 如果不用该算子，你有什么办法实现数据去重？
  *
  */
class SingleValueRDDTest3 {

  /*
      pipe :  允许使用shell脚本处理RDD中的数据！
              RDD调用Pipe时，每一个分区都会被 shell脚本进行处理！
   */

  /*
        sortBy: 全排序

        def sortBy[K](
      f: (T) => K,
      ascending: Boolean = true,  //提升，小到大
      numPartitions: Int = this.partitions.length)
      (implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T]
   */
  @Test
  def test8() : Unit ={

    val list = List(1,2,3,9,1,2,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    rdd.sortBy(x=>x,false).saveAsTextFile("output")


  }

  /*
        repartition
coalesce和repartition区别？
        repartition就是 shuffle=true的coalesce。
          repartition不管调大或调小分区，都会shuffle!

          建议：  调小分区，使用coalesce
                  调大分区，使用repartition
   */
  @Test
  def test7() : Unit ={

    val list = List(1,2,3,9,1,2,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    //coalesce(numPartitions, shuffle = true)
    //rdd.repartition()
  }

  /*  coalesce: 合并
                    多分区  =>   少分区  ，没有shuffle
                    少分区 => 多分区   ， 有shuffle

                    coalesce 默认 不支持 由少变多！  由少变多，依然保持少的分区不变！

                    可以传入shuffle = true，此时就会产生shuffle！重新分区！

               总结：  ①coalesce默认由多的分区，合并到少的分区，不会产生shuffle
                      ②如果默认由少的分区，合并到多的分区，拒绝，还使用少的分区
                      ③由少的分区，合并到多的分区，开启shuffle=true


          def coalesce(numPartitions: Int, shuffle: Boolean = false,
               partitionCoalescer: Option[PartitionCoalescer] = Option.empty)
              (implicit ord: Ordering[T] = null)
      : RDD[T]


      窄依赖：  当一个父分区，经过转换，产生一个子分区，此时称子分区窄依赖父分区！
                    独生子女

      宽依赖：   当一个父分区，经过转换，产生多个子分区，此时称宽依赖父分区！
                  Wide or shffle Dependencies

  我想要扩大分区，怎么办？
        shuffle = true

   */



  @Test
  def test5() : Unit ={

    val list = List(1,2,3,9,1,2,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    rdd.coalesce(4,true).saveAsTextFile("output")

  }




  /*
     如果不用该算子，你有什么办法实现数据去重？
          groupby
   */
  @Test
  def test3() : Unit ={

    val list = List(1,2,3,9,1,2,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    val rdd1: RDD[(Int, Iterable[Int])] = rdd.groupBy(x => x)

    val result: RDD[Int] = rdd1.keys

    result.saveAsTextFile("output")


  }





  /*
     distinct: 有shuffle


    case _ => map(x => (x, null)).reduceByKey((x, _) => x, numPartitions).map(_._1)


    List(1,2,1)  =>  map(x => (x, null)) => List((1,null),(2,null),(1,null))

    List((1,null),(2,null),(1,null))  => reduceByKey((x, _) => x, numPartitions)

     List((1,null),(2,null),(1,null)) => 分组   List(1,List(null,null),(2,List(null))

     List(1,List(null,null),(2,List(null)) => reduceByKey((x, _) => x, numPartitions)   List(1,null),(2,null)

    List((1,null),(2,null)) => .map(_._1)  List(1,2)
   */
  @Test
  def test2() : Unit ={

    val list = List(1,2,3,9,1,2,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    // 去重后还是2个分区
    // rdd.distinct().saveAsTextFile("output")

    rdd.distinct(3).saveAsTextFile("output")

  }




  /*
  sample: 抽样。返回当前RDD的抽样子集！
 *        withReplacement: Boolean   代表rdd中的元素是否允许被多次抽样！
 *              true： 一个元素有可能被多次抽到， 使用PoissonSampler算法抽取！
 *              false:  一个元素只能被抽到1次，使用BernoulliSampler算法抽取！
 *
 *        fraction: 样本比例。   样本大小 /  RDD集合大小  ， 控制 [0,1]  大致！
 *
 *        seed:  传入一个种子，如果种子，每次抽样的结果一样！
 *                如果想获取随机的效果，种子需要真随机！
 *                默认就是真随机！
 *
 *       在一个大型的数据集抽样查询，查看存在数据倾斜！
 *          某一个RDD中，有大key

        不会改变分区，也没有shuffle!
 *
 * def sample(
 * withReplacement: Boolean,
 * fraction: Double,
 * seed: Long = Utils.random.nextLong): RDD[T]
   */
  @Test
  def test1() : Unit ={

    val list = List(1,2,3,4,5,6,7,8)

    val rdd: RDD[Int] = sc.makeRDD(list, 2)

    /* println(rdd.sample(false, 0.5,2).collect().mkString(","))
     println(rdd.sample(false, 0.5,2).collect().mkString(","))
     println(rdd.sample(false, 0.5,2).collect().mkString(","))
     println(rdd.sample(false, 0.5,2).collect().mkString(","))*/

    rdd.sample(false, 0.5,2).saveAsTextFile("output")

  }

  val sc = new SparkContext(new SparkConf().setAppName("My app").setMaster("local[*]"))

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
