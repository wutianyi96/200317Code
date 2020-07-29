package com.atguigu.scala.day06

/**
  * Created by VULCAN on 2020/7/4
  *
  *        如果调用的是有参的构造器(主，辅助)，目的都是将在程序外部传入给构造器的参数
  *        赋值给类要构造的对象的属性！
  *
  *
  *        scala可以允许在主构造器上，标识 val|var 将构造器的参数自动变为属性！
  *        val 标识的属性，只读
  *        var 标识的属性，可读可写
  *        什么都不加，参数就是一个普通参数！ 在类的内部可以用，外部无法访问！
  *
  *
  *        辅助构造器的参数就仅仅是参数！
  *        如果希望通过辅助构造器给属性赋值，得声明属性，在辅助构造器中赋值！
  *
  *
  *        对象是如何构建的？
  *             val swag = new Swag("蛇皮蛙", 20, "雌")
  *
  *             ①先加载swap类到方法区
  *             ②在堆空间上，为对象分配一片内存，在内存先完成属性的空间分配
  *             ③如果有父类，先初始化父类的主(辅助)构造
  *             ④执行当前类的主(辅)构造
  *             ⑤完成属性的赋值等初始化操作
  *             ⑥将new的对象的内存地址，赋值给swag变量
  *
  *
  */
import  org.junit._
class ConstructorPassParameterTest {

  @Test
  def test() : Unit ={

    val swag = new Swag("蛇皮蛙", 20, "雌")

    println(swag.name) // swag.name()

    swag.age = 10
    println(swag.age)

    println(swag.toString)

  }

  @Test
  def test2() : Unit ={

    val swag = new Swag(20)

    println(swag.age)
    println(swag.legs1)


  }
}

// 在主构造器上，标识 val|var 将构造器的参数自动变为属性
class Swag(val name : String, var age:Int,sex:String ){

  //属性
  /*var name1 : String =_
  var age1 : Int =_
  var sex1 : String =_

  // 将构造器传入的参数赋值给属性
  name1=name
  age1=age
  sex1=sex*/

  //方法
  override def toString: String = {

    name + age + sex

  }

  // 明确声明属性 (辅助构造器的参数就仅仅是参数，想给属性赋值必须另外声明属性)
  var legs1 : Int = _

  def this( legs : Int)={

    this("白色",legs+10,"雄")
    println("参数列表为name : String的辅助构造器被调用了！")

    this.legs1 = legs

  }


}
