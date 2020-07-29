package com.atguigu.scala.day09

/**
  * Created by VULCAN on 2020/7/8
  *
  *    隐式转换：    多行字符串在声明后，可以调用  stripMagin(),此方法在String中并不存在！
  *                  编译器对String 进行隐式(偷偷地，默默地)转换，转换后将String转换为StringOps
  *                  调用StringOps的stripMagin
  *
  *
  *
  *     练习：   为File类添加读取每一行内容且输出的功能
  * 期望功能如下： 当输入  2 days ago ,返回2天前的日期
  * 输入 2 days later 返回2天后的日期
  *
  */

import java.io.File
import java.time.{LocalDate, LocalDateTime}

import org.junit._

import scala.io.Source

class MyFile(f:File){

  def printAllLines() ={
    Source.fromFile(f).getLines().foreach(println)
  }

}

class MyInt(x:Int){

  def days(suffix : String)={
    if (suffix =="ago"){

      LocalDate.now().minusDays(x)
    }else{
      LocalDate.now().plusDays(x)
    }

  }


}

/**
implicit如果要修饰一个隐式类，这个类不能是顶级类（最外层的类）
        隐式类的定义需要放入方法的内部，类的内部！
  */

class ImplictConversionTest {



  /**
      隐式类：   调用隐式函数进行转换，必须遵循一定的套路！
                将套路进行封装，封装为隐式类！

                隐式类 = 自定义类 + 隐式转换函数

                隐式类可以帮助我们省略隐式转换函数！
                  工作原理：  编译器会自动寻找作用域内的所有的隐式类，根据隐式类的参数类型和方法，进行匹配
                  如果可以匹配，自动隐式转换！

                  注意：隐式类的主构造器必须有参数列表！

                  隐式类的声明的位置： ①不能作为顶层类
                                     ②声明在类的内部，方法的内部，包对象中，(要转换的类的伴生对象中)
                                        任意一个伴生对象中，通过静态引入！

   */
  @Test
  def test6() : Unit ={

    //静态引入
    import com.atguigu.scala.day09.ImplicitObject._

    new File("input/word.txt").printAllLines1();
  }



  /**
  Int类型转自定义类：
		期望功能如下： 当输入  x days ago ,返回x天前的日期
    * 输入 2 days later 返回2天后的日期


                    x days ago  =>  x天前的日期

                    2.days("ago") = 2 days "ago"

               本质： 为2所代表的Int类，扩展一个days(),传入参数返回不同的日期！
    */
  @Test
  def test5() : Unit ={

    implicit  def Int2MyInt(x:Int) = new MyInt(x)

    println(2.days("ago"))
    println(2 days "ago")
    println(2 days "later")
    /*
    2020-07-07
    2020-07-07
    2020-07-11
    */

  }



  /**
  File文件转自定义类
       为File类添加读取每一行内容且输出的功能
    */
  @Test
  def test3() : Unit ={

    implicit  def File2MyFile(f:File) = new MyFile(f)

    new File("input/word.txt").printAllLines();

  }



  @Test
  def test1() : Unit ={

    /**
    显式转换：  Double.toInt
                    自定义方法  def f1(x: X) = Y
                      var y Y= f1(x)
         隐式转换：
                     自己定义一个规则，此规则可以告诉编译器怎么将一个Double转为Int,
                     希望编译器可以在将Double赋值给Int失败后，
                    自动调用规则，完成赋值！

                    使用implicit关键字标识此函数可以给编译器进行隐式转换

               注意事项：  ①编译器在调用隐式函数时，只考虑函数的参数列表和返回值类型是否复合要求
                              函数名可以任意

                          ②隐式函数可以定义在多个地方，必须保证程序在运行时，可以找到隐式函数
                              包对象，当前类体，当前方法中，当前类型数据的伴生对象类定义中

                          ③保证隐式转换函数不能有二义性, 当前匹配的范围内不能有相同功能的隐式转换函数


               作用：  ①完成一个类型到另一个类型的转换，完成一些赋值
                       ②利用隐式转换为类扩展功能

                            静态混入特质 / 继承类 ： 扩展类，改变了类的声明！   类名 extends / with 类名/特质名
                            动态混入特质：          扩展某个对象，不会改变类的声明   类名
                            隐式转换：              扩展类，不会改变类的声明    类名

                            非侵入式，复合OCP(对象设计的闭合原则)原则


      */



    implicit def Double2Int(x : Double) = x.toInt

    var i : Int = 3.1

    println(i)
  }

  /**
  自定义类转自定义类
    */
  @Test
  def test2() : Unit ={

    // 提供一个将Mysql类型转为DB类型的方法
    implicit def MySQL2DB(x:MySQL) = new DB()

    val mysql = new MySQL()

    //希望mysql类型的对象，可以调用DB中的方法
    mysql.insert()

  }

}
class MySQL{}

class DB{

  def insert()={
    println("insert")
  }

}