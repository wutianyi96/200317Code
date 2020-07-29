package com.atguigu.scala.day06

/**
 * Created by VULCAN on 2020/7/4
 *      apply() : 作用主要是为我们创建一个对象
 *                声明在类的伴生对象中！
 *
 *      使用：  伴生对象.apply()
 *                apply可以直接省略，简写为  伴生对象()
 *
 *       举例：  Range(1,5) : 返回一个[1,5)
 *              List(1,23,4,5) : 返回一个List集合
 *
 *        陷阱一：  如果调用空参的apply(),()不能省略！
 *
 *      apply: 就是一个方法，可以传入任意参数，返回任意内容！
 *
 *      返回某个类的一个单例的对象：
 *                ①主构造器私有
 *                ②提供一个公有的方法，每次调用这个方法，可以返回当前类的唯一实例
 *                ③保证唯一
 */

import  org.junit._
class ApplyTest {

  @Test
  def test1() : Unit ={

      val cow1 : Cow = Cow.apply()

      val cow2 = Cow()

      val cow3 = Cow(10)

      println(cow1.age)
      println(cow2.age)
      println(cow3.age)
    }

  @Test
  def test2() : Unit ={

      val cow2 : Cow = Cow()
//      cow2.hello()

      val cow : Cow.type = Cow
      cow.hello()
    }

  @Test
  def test3() : Unit ={

      val cow2 : Cow = Cow()

      val str : String = cow2()
      println(str)
    }

  @Test
  def test4() : Unit ={

      val bird:Bird = Cow("旱鸭子")
      Cow("jack",20)
    }

  @Test
  def test5() : Unit ={

      val cow1 = Cow()
      val cow2 = Cow()

      println(cow1 eq cow2)
    }
}

class Cow private(var age:Int){

    private def this() = {
      this(100)
    }

    def apply() = "hi"

}

object Cow{

    lazy val cow = new Cow

    println("伴生对象构造")

    def apply() : Cow = cow

    def apply(age:Int) :Cow = new Cow(age)

    def hello()={
      println("hello")
    }

    def apply(name:String) : Bird = new Bird(name)

    def apply(name:String,age:Int) : Unit = {
      println(name + age)
    }
}


