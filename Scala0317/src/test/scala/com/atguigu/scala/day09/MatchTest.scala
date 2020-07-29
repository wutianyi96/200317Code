package com.atguigu.scala.day09

/**
  * Created by VULCAN on 2020/7/8
  *
  * 模式匹配：  scala中没有swtich，使用模式匹配来模拟switch!
  * 模式匹配除了模拟switch，还有其他更强大的用法
  *
  * 没有break!
  *
  *
  * 常量匹配
  * 守卫
  * 类型匹配
  * Array匹配
  * 列表匹配
  * 元组匹配
  * 对象匹配
  */

import org.junit._

class MatchTest {

  /*
       i match : 类似于 switch(i)

       case:  类似于switch中的一个case分支！

       不能写break,不会出现分支穿透，只有匹配上一个就执行对应的代码，就不会再匹配其余的内容！
       匹配是从上而下进行匹配！
       一旦匹配不上，就跑错MatchError
   */
  @Test
  def test1() : Unit ={

    val i:Int = 3

    i match {

      case 1 => {
        println(1)
      }

      case 2 => println(2)

      //模拟java中的default
      //case i => println(i)

      // 当不需要接受变量时，可以省略变量名，使用_代替
      case _ => println("无")



    }

  }

  // 守卫
  @Test
  def test2() : Unit ={

    val i:Int = 4
    i match {
      case 1 => println(1)

      case 2 => println(2)

      // 匹配守卫，守卫成功，才算匹配成功，守卫语句返回false，匹配失败！
      case i if(i%2==1) => println("奇数"+i)
      case _ => println("无")
    }

  }

  /*
      类型匹配
   */
  @Test
  def test3() : Unit ={

    // 声明一个变量，知道变量对应的是什么类型
    val i:Any = 4
    i match {
      case b:Int =>  println(b+"是Int")
      case a:Double => println(a+"是Double")

      // 匹配守卫，守卫成功，才算匹配成功，守卫语句返回false，匹配失败！
      // case i if(i%2==1) => println("奇数"+i)
      case _ => println("无")
    }


  }

  @Test
  def test5() : Unit ={

    /**
    泛型是会擦除的,类型匹配时都是 例如：Map([Any], [Any])

        Array[T] : 是特殊的！
				对应Array来说，泛型是类型一部分！
					需要根据类型来确定数组初始化大小
						匹配时需要对应泛型类型顺序
							int [] x = new int [3]
							String []
      */
    val list = List(Map("K1" -> 1), Map(1 -> "v2"), List(1, 2, 3), List("a", "b", "c"), Array(1, 2, 3), Array("a", "b"))

    list.foreach( x => {

      x match {
        case a : Map[String,Int] => println(a+"Map[String,Int]")
        case b : Map[Int,String] => println(b+"Map[Int,String]")
        case c : List[String] => println(c+"List[String]")
        case d : List[Int] => println(d+"List[Int]")
        case e : Array[Int] => println(e+"Array[Int]")
        case f : Array[String] => println(f+"Array[String]")

        case _ => println("无")
        /*
        Map(K1 -> 1)Map[String,Int]
        Map(xx -> yy)Map[String,Int]
        List(1, 2, 3)List[String]
        List(a, b, c)List[String]
        [I@5abca1e0Array[Int]
        [Ljava.lang.String;@2286778Array[String]
        */

      }

    } )


  }

  // 模式匹配是可以有返回值
  @Test
  def test6() : Unit ={

    val i:Int = 2

    // 类型选择所有分支都可以匹配的共有父类
    val result: AnyVal = i match {
      case 1 => 1

      case 2 => 4

      // 匹配守卫，守卫成功，才算匹配成功，守卫语句返回false，匹配失败！
      case i if (i % 2 == 1) => println("奇数" + i) //返回的是unit类型，所以match返回的类型应该是AnyVal
      case _ => println("无")
    }

    println(result)

  }

  // Array匹配
  @Test
  def test7() : Unit ={

    val list = List(Array(0), Array(0, 1), Array(0, 2, 3), Array(0, 1, 2, 3, 4), Array(1, 1, 3))

    list.foreach(i=>{

      i match {

        //判断i是否和 case中的 Array(0) 匹配
        case Array(0) => println("当前是：" +Array(0).mkString(","))
        case Array(x,y) => println("当前是有两个参数的Array：" + x +"   "+y)

        // _代表任意元素
        case Array(0,_,2) => println("当前是Array(0,_,2)：")

        // 匹配第一个元素是1，之后有任意个元素
        case Array(1,_*) => println("当前是Array(1,_*)：")

        // 数组的第一个元素是0，a代表第二个变量，b@_*： _*代表有任意个参数  b@:  由b代表_*(任意个参数)
        case Array(0,a,b@_*) => println("当前是Array(0,a,b@_*)： a:"+a + "   :b:"+b.mkString(",") )
        /*
        当前是：0
        当前是有两个参数的Array:0 1
        当前是Array(0,a,b@_*): a:2 b:Vector(3)
        当前是Array(0,a,b@_*): a:1 b:Vector(2, 3, 4)
        当前是Array(1,_*):
        */

      }

    })

  }

  @Test
  def test8(): Unit = {

    val list = List(List(0), List(0, 1), List(0, 1, 2, 3, 4), List(1, 1, 3))

    list.foreach(i => {

      i match {

        //判断i是否和 case中的 Array(0) 匹配
        case List(0) => println("当前是：1")
        case List(x, y) => println("当前是有两个参数的List：" + x + "   " + y)

        // _代表任意元素
        case List(0, _, 2) => println("当前是Array(0,_,2)：")

        // 匹配第一个元素是1，之后有任意个元素
        case List(1, _*) => println("当前是List(1,_*)：")

        // 数组的第一个元素是0，a代表第二个变量，b@_*： _*代表有任意个参数  b@:  由b代表_*(任意个参数)
        case List(0, a, b@_*) => println("当前是Array(0,a,b@_*)： a:" + a + "   :b:" + b.mkString(","))
        /*
        当前是1
        当前是有两个参数的List：0 1
        当前是Array(0,a,b@_*): a:1 b:List(2, 3, 4)
        当前是List(1,_*):
        */

      }

    })

  }

  @Test
  def test9(): Unit = {

    val list = List(List(0), List(0, 1), List(0, 1, 2, 3, 4), List(1, 1, 3))

    list.foreach(i => {

      i match {

        //判断i是否和 case中的 Array(0) 匹配
        /**
        ::最右边一定是个集合
		Nil也是集合，只不过是空集合
          */
        case 0 :: a => println("1--  " + a)
        /*
        List(0)进来可以看成0和a(Nil类型)的空集合
        List(1, 1, 3))进不来，因为开头是1
        */
        case a :: Nil => println("2--  " + a)
        case a :: b :: Nil => println("3--  " + a + "  " + b)
        case a :: b :: c => println("4--  " + a + "  " + b + "  " + c)
        /*
        1--  List()
        1--  List(1)
        1--  List(1, 2, 3, 4)
        4--  1  1  List(3)
        */

      }

    })

  }

  @Test
  def test10(): Unit = {

    println(Nil)
    //List()
  }



  /**
  tuple的匹配
    */

  @Test
  def test11(): Unit = {

    val list = List((1, 0), (1, 2, 3), (1, 2, 3, 4), (2, 3, 4))

    list.foreach(x => {

      x match {

        case (1, a) => println("1-- " + "a:" + a)
        case (_, a) => println("2-- " + "a:" + a)
        case (_,a,b) => println("3-- " + "a:" + a + " b:" + b)
        case (a,b,c) => println("4-- " + "a:" + a + " b:" + b + " c:" + c)
        case _ => println("无")
        /*
        1-- a:0
        3-- a:2 b:3
        无
        3-- a:3 b:4
        */

      }

    })


  }

  /*
        变量匹配
   */
  @Test
  def test12(): Unit = {

    //   tuple:

    val tuple: (Int, Int, Int) = (1, 2, 3)

    //获取tuple的第二个元素
    println(tuple._2) //2

    // 使用模式匹配来获取tuple指定位置的参数  match 是模式匹配的一种使用形式！
    //  模式(样式) 匹配（对应）

    val (a, b, c) = (1, 2, 3)

    println(b) //2


  }

  @Test
  def test13(): Unit = {

    // List
    val list = List(1, 2, 3)

    println(list(1)) //2

    // 使用模式匹配
    val List(a, b, c) = List(1, 2, 3)

    println(b) //2

    val List(a1, b1, c1@_*) = List(1, 2, 3, 4, 5, 6, 7)

    println(c1)  //List(3, 4, 5, 6, 7)

    // 使用模式匹配
    val list1 = List(((((1, 2), 3), 4, 5), 6, 7), ((((11, 22), 33), 44, 55), 66, 77))

    println(list1(0)._1._3) //5

    list1.foreach(
      {
        case ((((_, _), _), _, y), _, _) => print(y + " ")  //5 55
        case _ => println("匹配不到")

      })


  }

  // 匹配Map
  @Test
  def test23(): Unit = {

    val map = Map("K1" -> (1, 2), "K2" -> (3, 4), "K3" -> (5, 2))

    map.foreach({
      case (_, (_, a)) => println(a)	//2		4	  2
    })

  }

  // for表达式模式  map  对Map循环遍历，获取想要获取的数据
  @Test
  def test24(): Unit = {

    val map = Map("K1" -> (1, 2), "K2" -> (3, 4), "K3" -> (5, 2))

    // elem代表Map 的一个元素
    for (elem <- map) {}
    /*
    (K1,(1,2))
    (K2,(3,4))
    (K3,(5,2))
    */

    for ((k,v) <- map) {
      print(k + " ");println(v)
    }
    /*
    K1 (1,2)
    K2 (3,4)
    K3 (5,2)
    */

    println("----------------")
    for ((k, (v1, v2)) <- map) {
      println(v2)

    }

    println("----------------")
    for ((k, (v1, v2)) <- map if (v2 > 2)) {
      println(v2) //4

    }

  }

  @Test
  def test26(): Unit = {

    /*
          var list1 =List(0,1)

          list1 match {

          //调用List.unapply
          case List(a,b) =>  println(a,b)
          }

     */
    val s1: Snake = Snake("花蛇", 20)
    val s2: Snake = Snake("草花蛇", 10)
    val s3: Snake = Snake("花蟒蛇", 20)
    val list = List(s1, s2, s3)

    /*
           在match语句中，case后，编写 伴生对象名(),不代表要调用 伴生对象的apply()，代表要调用 伴生对象的unapply()

           apply():  根据方法传入的参数，创建对象，为对象封装属性！
           unapply():  反向apply , 从对象，拆分属性！


           对象匹配的流程：  val s1 =Snake("花蛇", 20) ---->  Snake.unapply( s1) ----> Option
                              对Option进行判断，如果为None，代表匹配失败！
                                 如果为Some(name,age )，将Some中的name 赋值Snake(a,b) 中的a
                                                       将some中的age 赋值给b
        */

    list.foreach( x=>
      x match {
        //case Snake(a,10) => println("哈哈！"+a )

        //只想匹配age >10的蛇
        case Snake(a, b) if (b > 10) => println("哈哈！" + a + b)
        case _ => println("不符合资格！")
        /*
        哈哈!花蛇20
        不符合资格
        哈哈!花蟒蛇20
        */

      })


  }

  @Test
  def test31() : Unit ={

    val s1: CaseSnake = CaseSnake("花蛇", 20)
    val s2: CaseSnake = CaseSnake("草花蛇", 10)
    val s3: CaseSnake = CaseSnake("花蟒蛇", 20)
    val list = List(s1, s2, s3)

    // 样例类的unapply，直接返回Some(name,age)
    list.foreach( x=>
      x match {
        case CaseSnake(a,b) => println("哈哈！"+a +b)

        case _ => println("不符合资格！")
        /*
        哈哈!花蛇20
        不符合资格
        哈哈!花蟒蛇20
        */
      })

  }

  /*
      偏函数：   本质是函数，只对集合中的部分数据进行计算！
                在某些特殊的场景下，不需要对集合中的所有元素都进行计算！
          以偏概全

          偏：部分
          全： 全部

          map() : 对集合中的每一个元素都进行计算！
          foreach(): 全函数
   */
  @Test
  def test32() : Unit ={

    // 过滤出所有的偶数
    val list3 = List(1, 2, 3, 4, "abc", null, false)

    //别用foreach，没有返回值
    //不能直接map
    val list: List[Int] = list3.map({
      //如果是Int类型,而且是偶数,返回i
      case i: Int if (i % 2 == 0) => i
      case _ => 0
    })

    //美中不足就是局限累加的场合，如果是其他场合可能还需要进行过滤等！
    // List(0, 2, 0, 4, 0, 0, 0)
    println(list.sum) //6



    // 定义偏函数 PartialFunction[输入的数据类型(参考集合中数据的类型)，输出的数据类型]：
    //      只对集合的部分元素进行计算

    val myPF : PartialFunction[Any,Int]={
      // 函数的计算逻辑
      // 运用了模式匹配
      case i: Int if (i % 2 == 0) => i
    }

    // 使用： 偏函数是作为参数传入到集合的高阶函数中，不是所有的高阶函数都支持偏函数
    // map不支持
    // 偏函数 效果等同于 = filter ----->map

    val result: List[Int] = list3.collect(myPF)

    // List(2, 4)
    println(result)
    // 8
    println(result.product)


  }

}

class Snake(val name: String, val age: Int) {}

object Snake {

  def apply(name: String, age: Int): Snake = new Snake(name, age)

  /*
      提供拆分属性的unapply
          unapply: 传入一个Snake对象，拆分(name,age)
            将(name,age) 　封装到Option中！

        封装到Option的目的是为了和模式匹配结合！
            Option　：　　Some:  一定有值
                          None：　一定没有值

             如果Option返回None，代表匹配不成功。
             如果Option返回Some，代表匹配成功！此时自动将Some(name,age)取出
             赋值给case语句块中声明的变量！
   */

  /*
      只返回age > 10的蛇的属性
   */
  def unapply(arg: Snake): Option[(String, Int)] = {

    // if (arg.age > 10) Some(arg.name, arg.age) else None

    Some(arg.name,arg.age)

  }

  /*
      样例类 ： 使用case修饰的类，称为样例类
            scala自动为样例类生成一套模版
                getter,setter
                toString,hashcode,equals
                apply,unapply
                序列化接口

       直接在模式匹配中使用！
   */



}

//样例(模版)类
// 样例类，默认属性为val，如果需要改为var
case class CaseSnake(name: String, age: Int) {}
