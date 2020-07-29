package com.atguigu.scala.day08

/**
  * Created by VULCAN on 2020/7/7
  *
  * map
  * flatten
  * flatmap
  * filter:val list = List(1, 2, 3, 4, "abc", null, false)计算所有偶数的和
  * groupby
  * sortby
  * 自定义排序
  * wordcount
  * reduce   求集合中的最小值
  * fold   将字符串的每个字符放入到一个ArrayBuffer中
  * 统计字符出现的次数
  * 将两个Map进行合并后扁平化
  * scan
  * zip  zipall zipWithIndex unzip
  * sliding
  * 线程安全集合
  * Map.mapValues
  * 并行
  */

import org.junit._

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.io.{BufferedSource, Source}

class CollectionOperateTest {

  /**
      并行：  多个线程同时运行，各自使用各自的资源！
            scala支持集合并行运算！

      并发：   多个线程同时抢占一个资源
   */
  @Test
  def test25() : Unit ={

    val list1 = List(1, 2, 3, 4,5,6,7,8,9,10)

    //单线程
    list1.foreach(println)

    println("------------------------------")

    // 并行
    list1.par.foreach(println)

  }

  /**
    *	mapValues
    *	对一个Map的所有value进行函数操作，
    *		之后将函数返回的值和Map中每个value的key再组合为新的Map
    */
  @Test
  def test24() : Unit ={

    //
    val map = Map(("k1", 1), ("k2", 2))

    val map1: Map[String, Int] = map.mapValues(x => x + 10)

    //Map(k1 -> 11, k2 -> 12)
    println(map1)

  }

  /**
  线程安全集合
          如何满足线程安全：
              ① 单线程
              ②  多个线程访问是只读变量
              ③  访问的变量是在方法中声明的
              ④  每个线程都访问自己的数据
              ⑤  尝试使用一些线程安全的类
                      Synchronizedxxx



    */

  /*
      sliding : 平滑
   */
  @Test
  def test23() : Unit ={

    val list1 = List(1, 2, 3, 4,5,6,7,8,9,10)

    val iterator: Iterator[List[Int]] = list1.sliding(2, 3)
    println(iterator)
    //<iterator>

    val reuslt: List[List[Int]] = iterator.toList
    //List(List(1, 2), List(4, 5), List(7, 8), List(10))

    println(reuslt)

  }

  @Test
  def test22() : Unit ={

    //zip  zipall zipWithIndex unzip  拉锁
    // 将两个集合，相同位置的元素，进行组合，组合为一个 (x,y),返回所有对偶元组的集合
    val list1 = List(1, 2, 3, 4)
    val list2= List('a','b' , 'c', 'd')

    //List((1,a), (2,b), (3,c), (4,d))
    println(list1.zip(list2))

    // 只能是对偶
    val list3 = List(1, 2, 3 ,4,5)
    val list4= List('a','b' , 'c', 'd')

    //List((a,1), (b,2), (c,3), (d,4))
    println(list4.zip(list3))

    // 全拉链
    //List((1,a), (2,b), (3,c), (4,d), (5,z))
    println(list3.zipAll(list4, 0, 'z'))

    // zipWithIndex
    val list6 = List(1, 2, 3 ,4,5)

    // List[(element,index),...]
    //List((1,0), (2,1), (3,2), (4,3), (5,4))
    println(list6.zipWithIndex)

    // unzip 反拉链
    val list7 = List((1, 'a'), (2, 'b'))

    val list8: (List[Int], List[Char]) = list7.unzip

    println(list8)
    println(list8._1)
    println(list8._2)
    /*
    (List(1, 2),List(a, b))
    List(1, 2)
    List(a, b)

    */

  }

  /**
  scan : 及fold折叠 过程中每一次计算的值都进行保存为集合

        记录foldleft :  scanleft
        记录 foldright : scanright
    */
  @Test
  def test21() : Unit ={

    val list = List(1, 2, 3, 4)

    //  (((10-1)-2)-3)-4

    val result: List[Int] = list.scanLeft(10)((x, y) => x - y)

    println(result)


  }

  @Test
  def test20() : Unit ={

    //将两个Map进行合并后扁平化
    val map1 = mutable.Map(("k1", 1), ("k2", 2), ("k3", 3), ("k13", 13))
    val map2 = Map(("k11", 1), ("k2", 2), ("k3", 3))

    // 期望结果返回一个Map :  Map(("k1", 1),("k2", 4))
    println(map2.foldLeft(map1)((map, kv) => {
      map.put(kv._1, map.getOrElse(kv._1, 0) + kv._2)
      map
    }))
    /*
    Map(k2 -> 4, k11 -> 1, k1 -> 1, k13 -> 13, k3 -> 6)
    */


  }

  @Test
  def test19() : Unit ={

    //统计字符出现的次数  期望结果  Map{(a,3)(b,4)}
    val s: String ="abcdefaefawfa"

    val map: mutable.Map[Char, Int] = mutable.Map()

    println(s.foldLeft(map)((map, char) => {
      // 如果key不存在，写入
      // 如果key存在，更新  获取字符在map中已经存的value+1, 如果不存在，用0代替
      // map.getOrElse(char, 0) + 1
      map.put(char, map.getOrElse(char, 0) + 1)//利用put修改的功能
      map
    }))
    //Map(w -> 1, e -> 2, b -> 1, d -> 1, a -> 4, c -> 1, f -> 3)

  }

  @Test
  def test18() : Unit ={

    //将字符串的每个字符放入到一个 (ArrayBuffer数组缓冲区) 中
    val s: String = "abcdefghijklmnopqrstuvwxyz"

    //方式一：
    // 使用扁平化
    //Vector(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z)
    println(s.flatten(x => ArrayBuffer(x)))

    //方式二：
    // 折叠
    val buffer = new ArrayBuffer[Char]()
    println(s.foldLeft(buffer)((buffer, char) => {
      buffer.append(char)
      buffer
    }))
    //ArrayBuffer(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z)
    println(buffer)

  }

  @Test
  def test17() : Unit ={

    // 使用reduce  求集合中的最小值
    val list = List(1, 2, 3, 4)

    println(list.reduce((x, y) => if (x < y) x else y))

  }

  /**
  fold : 折叠
              允许 集合中的每一个元素和集合外的一个元素，进行reduce运算

              初始值必须和集合的元素类型一致！

      foldLeft:   def foldLeft[B](z: B)(op: (B, A) => B): B
                    允许初始值为一个其他类型，
                    函数运算后，返回的是初始值类型！
                fold
                    从左向右计算！

      foldRight:
                    从右向左计算！ 函数中的参数反转！
    */
  @Test
  def test16() : Unit ={

    val list = List(1, 2, 3, 4)

    //  (((10-1)-2)-3)-4
    println(list.fold(10)((x, y) => x - y))

    println(list.foldLeft("haha")((x, y) => x + y))

    // (1, 2, 3, 4) 1
    //  1-(2-(3-(4-1)))
    println(list.foldRight(1)((x, y) => x - y))

    /*
    0
    haha1234
    -1
    */
  }

  /**
  reduce : 归约
          def reduce[A1 >: A](op: (A1, A1) => A1): A1 = reduceLeft(op)
              要求函数必须返回集合中类型的结果！
              reduce最终只能返回集合中类型的结果！


       reduceLeft: reduce，计算顺序从左向右！

       reduceRight : 计算顺序从右向左！两两运算！
    */
  @Test
  def test15(): Unit = {


    val list = List(1, 2, 3, 4)

    // ((1-2)-3)-4
    val reuslt: Int = list.reduce((x, y) => x - y)

    println(reuslt)

    // ((4-3)-2)-1
    val reuslt2: Int = list.reduceRight((x, y) => x - y)

    println(reuslt2)

  }

  @Test
  def test14(): Unit = {

    val list = List(1, 2, 3, 4)

    //val list1: List[Int] = list.flatten(x => List(x))

    val list1 = List(List(1, 2, 3, 4), List(1, 2, 3, 4))

    println(list1.flatten(x => x))

  }

  @Test
  def test13(): Unit = {

    /**
    思路：
        wordcount : 统计单词出现频率最高的前2名

        ①word.txt  =>  读
        ②将读的数据按行放入到集合中   List[String] : 每行内容组成的集合
              (hello hello hello , hi hello hi hello )
        ③List[String] 进行扁平化   List[String] :  由每个单词组成的集合
            ( hello,hello,hello,.... )
        ④按照单词的名称进行分组  groupBy
            Map[单词,List(单词，单词，单词)]
        ⑤ Map[单词,List(单词，单词，单词)]   (单词,List(单词，单词，单词)) =>map  (单词，数字)
            List((单词,10),(单词,20))
        ⑥排序
            List((单词，20),(单词，10))
        ⑦取前2
            take(2)


      */

    //①word.txt  =>  读
    val source: BufferedSource = Source.fromFile("input/word.txt")

    //将读的数据按行放入到集合中   返回每行内容组成的集合
    val lines: List[String] = source.getLines().toList

    // 将每行中的单词拆开，独立放入一个新的集合  扁平化
    val words: List[String] = lines.flatMap(line => line.split(" "))

    // 分组   Map[单词名，List(单词,单词，单词)]
    val wordMap: Map[String, List[String]] = words.groupBy(word => word)

    // Map[单词名，List(单词,单词，单词)] =>  Map(单词名， 词频)
    //Map(nice -> 2, spark -> 2, scala -> 3, hi -> 3, hello -> 6)
    val map: Map[String, Int] = wordMap.map(kv => (kv._1, kv._2.length))

    //转List,不然Map调用方法时容易覆盖，List比较方便
    //List((nice,2), (spark,2), (scala,3), (hi,3), (hello,6))
    val result: List[(String, Int)] = map.toList

    println(result)

    // 排序取前2
    //List((hello,6), (scala,3))
    println(result.sortBy(entry => entry._2)(Ordering.Int.reverse).take(2))


  }

  /**
  map:  def map[B](f: A => B): TraversableOnce[B]
               1对1映射！
                将一个集合中的每一个A类型元素，通过f:(A=>B)计算后，将计算的B类型元素，重新放入到集合中！返回新的B类型的集合！

    */
  @Test
  def test1() : Unit ={

    val list = List(1, 2, 3, 4)

    val list1: List[Int] = list.map(x => x + 1)

    println(list1)

    val list2: List[String] = list.map(x => "hello:" + x)

    println(list2)
    /*
    List(2, 3, 4, 5)
    List(hello:1, hello:2, hello:3, hello:4)
    */

  }

  /**
  flatten（展平）: A =>  GenTraversableOnce[B]

      扁平化： 将集合中的每个元素，取出放入新的集合！
    */
  @Test
  def test2() : Unit ={

    val list = List(1, 2, 3, 4)

    val list1: List[Int] = list.flatten(x => List(x))

    // List(1, 2, 3, 4)
    println(list1)

    val list2 = List(List(1, 2, 3), List("haha", 5, 6))

    val list3: List[Any] = list2.flatten(x => x)

    println(list3)

    // 注意：  不能嵌套太多
    val list4 = List(
      List(List(1,2), List(2,3), List(3,4)),
      List(List(1,2), List(2,3), List(3,4))
    )

    // 只能将第一层集合进行扁平化
    val list5: List[List[Int]] = list4.flatten(x => x)

    println(list5)
    /*
    List(1, 2, 3, 4)
    List(1, 2, 3, hello, 5, 6)
    List(List(1, 2), List(2, 3), List(3, 4), List(1, 2), List(2, 3), List(3, 4))
    */


  }

  // flatmap :   map + flatern  先map再扁平化
  @Test
  def test3() : Unit ={

    val list2 = List(ListBuffer(1, 2, 3), ListBuffer(4, 5, 6))

    val result: List[Int] = list2.flatMap((x: ListBuffer[Int]) => {
      x += 100
    })

    println(result)

    // string也是集合，是字符集合
    val wordList: List[String] = List("hello world", "hello atguigu", "hello scala")

    // hello world ==> map为    Array[String](hello,world)
    val list: List[String] = wordList.flatMap(x => x.split(" "))

    println(list)




  }

  /**
  filter 过滤:  def filter(p: A => Boolean)
     val list = List(1, 2, 3, 4, "abc", null, false)计算所有偶数的和
    */
  @Test
  def test4() : Unit ={

    val list1 = List(1, 2, 3, 4)

    val list2: List[Int] = list1.filter(x => x > 2)

    println(list2)

    // val list : List[Any]= List(1, 2, 3, 4, "abc", null, false)计算所有偶数的和

    // 过滤出所有的偶数
    val list3 = List(1, 2, 3, 4, "abc", null, false)

    // 过滤所有的Int类型
    val list: List[Any] = list3.filter(
      x => {
        x.isInstanceOf[Int]
      }
    )

    //将Any => Int
    val list4: List[Int] = list.map(x => x.asInstanceOf[Int])

    println(list4.filter(x => x % 2 == 0).sum)
    /*
    List(3, 4)
    6
    */


  }

  /**
  groupby : def groupBy[K](f: A => K): immutable.Map[K, Repr]

          调用f对集合的每一个元素进行运算，将运算后的结果，作为key，对集合中的元素进行分组！
           分组的结果是个Map,Key是函数运算后的值，value是分组后元素的结合
    */
  @Test
  def test5(): Unit = {

    val list1 = List(1, 2, 3, 4, 1, 1)

    println(list1.groupBy(x => x))

    // 1, 2, 3, 4 ,1,1  ==> f ==>  false,false,false,true,false,false ==> Map(false->(1,2,3,11 ), true->(4) )
    println(list1.groupBy(x => x > 3))
  }

  /**
  sortby : 由xxx排序 def sortBy[B](f: A => B)(implicit ord: Ordering[B]): Repr

          将集合的每一个元素，经由 f函数计算后的结果B，对B进行排序，排序后输出B对应的A的顺序

          List(11, 2, -3, 4) => f(x=>x)  List(22, 4, -6, 8)

           List(22, 4, -6, 8) ===> B(-6,4,8,22)
                                   A(-3,2,4,11 )
    */
  @Test
  def test7(): Unit = {

    val list1 = List(11, 2, -3, 4)

    // 升序
    val list: List[Int] = list1.sortBy(x => x)

    // 升序的反转就是降序
    println(list.reverse)

    println(list)

    // 降序
    /*
        (11, 2, -3, 4) =>  f => (-11,-2,3,-4)  从小到大 (-11,-4,-2,3)
     */
    val list2: List[Int] = list1.sortBy(x => -x)

    println(list2)


    /**
    默认是升序，修改默认的排序策略为降序
        def sortBy[B](f: A => B)(implicit ord: Ordering[B]): Repr

        如果希望改变排序策略，可以在第一个参数列表显式传递一个Ordering类型的对象，
        常用的AnyVal类型，scala已经提供了这些对象，不需要自定义

        Ordering.Int: 对Int类型进行升序排序
        Ordering.Int.reverse: 对Int类型进行降序排序
      */
    val list4: List[Int] = list1.sortBy(x => x)(Ordering.Int.reverse)

    println(list4)


  }

  @Test
  def test8(): Unit = {

    val list1 = List(11, 2, -3, 4)
    /*
        默认： 升序 List(11, 2, -3, 4) ==> f ==>  B(1,-8,-3,-6)  ==> B(-8,-6,-3,1) ==> A(2,4,-3,11 )

     */
    val list2: List[Int] = list1.sortBy(x => {
      if (x > 0) x - 10 else x
    })

    //List(2, 4, -3, 11)
    println(list2)

  }

  /**
  对集合中的 Person进行排序
          先按照name升序排序，如果name一样，继续按照age进行降序排序，如果age也一样，继续按照sex进行升序排序
    */
  @Test
  def test9(): Unit = {

    val persons = List(
      Person("jack", 18, '1'),
      Person("amy", 18, '1'),
      Person("amy", 18, '0'),
      Person("tom", 18, '1'),
      Person("tom", 19, '0'))

    // f(person) =>
    /*
        提供一个排序的对象 Ordering[Person] 进行排序
     */

    val order = new Ordering[Person] {

      // 比较两个对象，谁大谁小
      override def compare(x: Person, y: Person): Int = {

        //先按照name升序排序
        var result: Int = x.name.compareTo(y.name)

        //如果name一样，继续按照age进行降序排序
        if (result == 0) {

          result = -x.age.compareTo(y.age)

        }

        //如果age也一样，继续按照sex进行升序排序
        if (result == 0) {

          result = x.sex.compareTo(y.sex)

        }
        result
      }
    }

    println(persons.sortBy(x => x)(order))
    /*
    List(amy 18 0, amy 18 1, jack 18 1, tom 19 0, tom 18 1)
    */
  }


  @Test
  def test10(): Unit = {

    val persons = List(
      Person("jack", 18, '1'),
      Person("amy", 18, '1'),
      Person("amy", 18, '0'),
      Person("tom", 18, '1'),
      Person("tom", 19, '0'))

    // 复杂结构 Person(name,age,sex) ，将复杂对象的要比较的属性封装为tuple
    // Tuple类型，scala也提供了默认的比较方式，可以直接使用系统定义的Ordering[Tuple]
    // Ordering.Tuple2(Ordering.Int.reverse, Ordering.String)):  Tuple中的K降序排列，如果K一样，将V升序排序！
    println(persons.sortBy(p => (p.age, p.name))(Ordering.Tuple2(Ordering.Int.reverse, Ordering.String)))


    println(persons.sortBy(p => (p.age, p.name, p.sex))(Ordering.Tuple3(Ordering.Int.reverse, Ordering.String, Ordering.Char.reverse)))
    /*
    List(tom 19 0, amy 18 1, amy 18 0, jack 18 1, tom 18 1)
    */
  }

  /**
  sortWith : def sortWith(lt: (A, A) => Boolean): Repr

            sortBy(f): 先调用f，得到一个B，将B排序后，再找到B对应A输出！
            sortWith：  集合中的元素两两比较，只需要返回true,false！谁大谁小

                          x1 > x2 : 降序
                          x1 < x2 : 升序
    */
  @Test
  def test11(): Unit = {

    val list1 = List(11, 2, -3, 4)

    // 降序
    println(list1.sortWith((x1, x2) => x1 > x2))

    // 升序
    println(list1.sortWith((x1, x2) => x1 < x2))

  }

  @Test
  def test12(): Unit = {

    val persons = List(
      Person("jack", 18, '1'),
      Person("amy", 18, '1'),
      Person("amy", 18, '0'),
      Person("tom", 18, '1'),
      Person("tom", 19, '0'))

    println(persons.sortWith((p1: Person, p2: Person) => {
      //先按照name升序排序，如果name一样，继续按照age进行降序排序，如果age也一样，继续按照sex进行升序排序
      if (p1.name == p2.name && p1.age == p2.age) {
        p1.sex.compareTo(p2.sex) < 0
      } else if (p1.name.compareTo(p2.name) < 0) {
        true
      } else if (p1.name == p2.name) {
        p1.age > p2.age
      } else {
        false
      }

    }))

    /*
    List(amy 18 0, amy 18 1, jack 18 1, tom 19 0, tom 18 1)
    */
  }


}

class Person(var name: String, var age: Int, var sex: Char) {

  override def toString: String = name + " " + age + " " + sex

}

object Person {
  def apply(name: String, age: Int, sex: Char): Person = new Person(name, age, sex)
}