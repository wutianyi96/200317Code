package com.atguigu.scala.day06

/**
  * Created by VULCAN on 2020/7/4
  *
  *    如果子类继承了父类protected修饰的属性和方法，在scala中，子类的实例只能访问自己继承的属性和方法！
  *    无法调用父类实例中的属性和方法！
  *
  *
  *    重载： 方法名一样，参数列表不同(个数，类型，位置)
  *
  *
  *    重写：
  *          方法的重写：  普通方法的重写，必须声明override
  *
  *          属性的重写
  */
import  org.junit._

class ExtendTest {

  @Test
  def test1() : Unit ={

    val son = new Son1

    son.giveMoney(100)

  }

  @Test
  def test2() : Unit ={

    // 在方法重载中，传入的值类型如果无法匹配到对应的方法，
    //会自动隐式转换为高精度类型！(一级一级向上转合适就停止)

    // 在方法重载中，传入的是AnyRef类型，此时如果当前类型无法匹配到对应的方法，
    //会根据继承关系树向上寻找合适和父类型！

    //测试重载
    val x1 : Double = 20.0
    Son1.giveMoney(x1)

    // 没有Byte自动提升为short，一级一级提升
    val x2 : Byte = 20
    Son1.giveMoney(x2)

    // 如果不指定类型，默认参考 new 后面的类型
    val son:Son1 = new Son1
    // 获取对象类型
    println(son.getClass.getName)

    // 获取某个指定类型的Class实例
    //val sonClass: Class[Son1] = classOf[Son1]

    Son1.giveMoney(son)

  }
}

class Son1 extends Father1 {

  override def giveMoney(x: Int): Unit = super.giveMoney(x * 2)

}

class Father1{

  def giveMoney(x:Int)={

    println("爸爸给了:" + x)

  }

}

object  Son1{

  def giveMoney(x:Int)={

    println("(x:Int 爸爸给了:" + x)

  }

  def giveMoney(x:Double)={

    println("(x:Double 爸爸给了:" + x)

  }

  def giveMoney(x:Short)={

    println("(x:Short 爸爸给了:" + x)

  }

  def giveMoney(x:Father1)={

    println("(x:Father1 爸爸给了:" + x)

  }

  /*def giveMoney(x:Son1)={

    println("(x:Son1 爸爸给了:" + x)

  }*/

}


