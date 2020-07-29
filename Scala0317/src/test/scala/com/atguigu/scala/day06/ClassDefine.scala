package com.atguigu.scala.day06

/**
  * Created by VULCAN on 2020/7/4
  *
  *    类定义：
  *          Java:  [权限修饰符] class  类名 {
  *              类体
  *            }
  *
  *          在一个源文件中，只有有一个public 修饰的类！
  *
  *         scala :  class 类名{}
  *
  *           在一个源文件中,可以有多个公开的类！
  *
  *     属性：
  *            在类体中声明！
  *
  *            封装！  将属性私有！提供公有的get,set方法！
  *
  *
  *            Scala：  只需要声明属性，编译后自动生成对应属性的set,get方法！
  *                    val声明的，只有get
  *                    var声明的，以后set,get
  *
  *                    对属性的访问，本质上都是使用get,set方法实现！
  *
  *             scala生成的对属性操作的get,set和javabean规范不一样，在java领域很多框架
  *             都默认遵循使用反射获取属性，获取后，拼接get,set，调用拼接后的get,set方法名！
  *
  *             如果类由scala编写，编译后的字节码，如果希望使用Java中的这些框架，将无法适配！
  *
  *             解决： 使用@BeanProperty完成生态的兼容！
  *                    不能声明在私有属性上！矛盾！
  *
  *
  *     方法：
  *             在类体中声明！
  *
  *
  *      属性的默认值：
  *                java  :  引用数据类型   null
  *                        基本数据类型   0
  *                         boolean      false
  *
  *                scala:  显式赋值默认值！  _
  *                          如果赋值默认值，要求属性必须声明类型！
  *                          val修饰的变量不能使用_赋值，因为val不能被改写，付默认值就没有意义了
  *
  *                          AnyRef   null
  * *                        AnyVal   0
  * *                         Boolean      false
  *
  *
  *
  */
import org.junit._

import scala.beans.BeanProperty
class ClassDefine {

  @Test
  def test() : Unit ={

    val cat = new Pig

    /*cat.age=10  // 本质还是调用的方法  setAge ,实际调用是
    println(cat.name)  //  getName()   cat.name()

    cat.age

    cat.age_$eq(20)
    println(cat.age)

    println(cat.getAge())*/

    // 通过查看反编译文件，属性为protected的，虽然提供了公有的方法，但是在编译时，是无法通过的！
    // cat.age

    println(cat.ifClean)
    println(cat.sex)
    println(cat.age)


  }
}

/*
   反编译后的效果：
   private final String name = "猪";
   private int age = 20;

   public String name() {
      return this.name;
   }

   public int age() {
      return this.age;
   }

   public void age_$eq(final int x$1) {
      this.age = x$1;
   }
 */
class Pig{

  // 自动生成复合JavaBean规范的set,get
  // @BeanProperty
  private val name :String = "猪"

  var sex :String = _

  var ifClean :Boolean = _

  @BeanProperty
  var age :Int = _

  /* def  getAge():Int= {
     age;
   }*/

}
