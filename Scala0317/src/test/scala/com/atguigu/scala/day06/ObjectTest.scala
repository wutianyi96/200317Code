package com.atguigu.scala.day06

/**
 * Created by VULCAN on 2020/7/4
 *    object修饰的类，称为伴生对象。作用用来模拟java中static的场景！
 *        在object修饰的类中,定义的属性，都是属于类的属性！
 *                          定义的方法，都是属于类的方法！
 *
 *         在scala中，认为static关键字不复合完全面向对象的特征！
 *
 *         因此专门创建了object用于存放属于类的一些信息！
 *
 *  注意事项：
 *            ①伴生对象一般会和伴生类一起声明
 *                class xxx  object xxx
 *                在没有写伴生类时，系统也会自动生成
 *
 *            ②在编译时，生成
 *                伴生类的class文件    xxx.class
 *                伴生对象类的class文件   xxx$.class
 *
 *            ③伴生对象是单例的
 *
 *            ④ 通常将实例方法和属性，封装到class中
 *               将属于类的一些方法和属性，封装到object中
 *
 *            ⑤ 如果要声明伴生类
 *                  伴生类名和要伴生对象类名一致！
 *                  伴生类的声明要和伴生对象的声明放入一个源文件！
 *
 *           ⑥ 调用伴生对象中的属性和方法
 *                  伴生对象名.属性
 *                  伴生对象名.方法
 *
 *           ⑦伴生对象和伴生类可以互相访问其中定义的私有的属性和方法
 *
 *
 */
import  org.junit._
class ObjectTest {

  @Test
  def test1() : Unit ={

    Cat.helloTest()


  }

}


//伴生对象
object  Cat{

  def helloTest()={

    println("hello")

  }

  private val age = 20

  private  def hi()={
    println("hi")

    val cat = new Cat

    cat.name

   // cat.sayHello()
  }

}

class Cat{

   private val name = "cat"

   private def sayHello()={
     println("Miao")

     //使用伴生对象中的私有属性
     Cat.age

     Cat.hi()

   }


}


