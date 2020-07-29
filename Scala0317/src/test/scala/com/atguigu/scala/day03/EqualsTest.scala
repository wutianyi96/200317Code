package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 *    介绍Scala中 常见的判断两个对象相等的方法
 */

import org.junit._

class EqualsTest {

  /**
  Java
        基本数据类型：   ==
        引用数据类型：   ==(比较地址值)
                        Object.equals(Object e) : 默认 ==
                        可以重写，使用自己定义的逻辑

    Scala :   Any:
                    使用==进行运算时，默认就调用了equals，所以二者等价

              AnyRef(所有的引用数据类型的父类):   类似于Java中的Object
                     def equals(that: Any): Boolean = this eq that :  equals 等价于 eq
                     final def eq(that: AnyRef): Boolean = sys.error("eq"): 比较地址值， 等价于Java中的==

                   final def ==(that: AnyRef): Boolean =
                   if (this eq null) that eq null
                   else this equals that        先判断当前的对象是否为null，如果不为null，调用equals



              AnyVal:  不管调用==,equal，都是判断值是否相等！


       总结：  AnyVal：  用==
               AnyRef:  默认  ==,equals,eq 效果一样，都是比较地址值
                  用哪个？
                     如果要比较地址值，用 eq
                     要比较两个对象是否相等(自定义逻辑)，用==



   */
  @Test
  def test1() : Unit ={

     val i = 4
    val j =3

    println(i == j)

  }

  @Test
  def test2() : Unit ={

    val dog1 = new Dog
    val dog2 = new Dog

    println("==:"+ (dog1 == dog2))
    println("equals:"+ (dog1 equals dog2))
    println("eq:"+ (dog1 eq dog2))

  }

  @Test
  def test3() : Unit ={

    var dog1 = new Dog
    val dog2 = new Dog

    //比较地址值，是否是同一个对象
    println("eq:"+ (dog1 eq dog2))

    // 比较内容
    //dog1 = null

    println("==:"+ (dog1 == dog2))

    // 如果使用equals，可能会有空指针异常
    println("equals:"+ (dog1 equals dog2))


  }
}

class Dog{

  val id = 1

  /*
      isInstanceOf[T0]:  java的 instanceOf 判断是否是TO类型
       asInstanceOf[T0]：  将xx类型转换为To类型的一个实例
   */
  override def equals(obj: Any): Boolean = {

    // 先判断比较的是不是Dog类型，如果不是Dog类型，返回false
    if (! obj.isInstanceOf[Dog] ){

      false

    }else{

      //转换为Dog类型
      val dog: Dog = obj.asInstanceOf[Dog]
      //继续根据id进行判断
      this.id==dog.id
    }

  }


}
