package com.atguigu.scala.day07

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
  *          属性的重写：
  *                      在java中没有属性的重写，父类和子类是可以定义同名同类型的属性。
  *                      子类继承父类后，就拥有两个同名的属性！ 将父类中的同名属性称为隐藏属性！
  *
  *                      在scala中有属性重写！
  *                          ①val修饰的属性，只能重写被val修饰的属性
  *                          ②val修饰的属性，还可以重写一个空参的同名方法
  *                          ③var修饰的属性，只能重写另一个抽象的var修饰的属性
  *
  *                              抽象的属性： 只声明参数名称类型，不赋值
  *                                          类名必须使用abstract修饰!
  *                                          有抽象属性的类，称为抽象类！
  *                                          抽象类中不一定有抽象属性！
  *
  *
  *    在scala中，可以为任意修饰的属性或方法，添加一个包权限，只要同包的，都可以访问！
  */
import org.junit._

class ExtendTest {

  @Test
  def test1() : Unit ={

    val son : Son2 = new Son2

    println(son.name)

    println(son.hello)

    // println(son.age)

    val son1 : Father2 = new Son2

    println(son1.name)

    println(son1.hello())

    //not ok
    /* println(son1.address)
     println(son.address)*/


  }


}

class Son2 extends Father2 {

  override val name : String ="儿子"

  // 参数名要和方法名一致，参数类型和方法返回值类型一致
  override val hello : String ="子类的hello"

  // 重写抽象属性
  override var age: Int = _

  override val gender: String = "女"

  //override val address :String = "sz"
}

abstract class Father2{

  // 添加包权限
  /*protected [day07] val name : String ="爸爸"

  private [day07] val age : Int = 20*/

  val name : String ="爸爸"

  def hello():String={
    "hello"
  }

  var age :Int

  val gender :String

  // var修饰的可变属性无法被覆写
  var address :String = "sz"

}



