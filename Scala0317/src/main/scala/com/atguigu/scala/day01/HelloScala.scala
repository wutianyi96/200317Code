package com.atguigu.scala.day01

import scala.io.StdIn

/**
 * Created by VULCAN on 2020/6/28
 */
object HelloScala {

  /*
       1.object 是一个标识符，用来标识一个伴生对象（ 可以暂时简单理解为一个对象）。
                 scala是一个完全面向对象的语言，不支持static。
                 作用： 用来模拟static的效果！
                        伴生对象中的方法可以直接使用 伴生对象名.方法名()调用！

        2.def main(args: Array[String]): Unit
            def :  是英文 define 或 defind 的缩写，用来标识定义一个方法/函数
            (args: Array[String])：  args 为参数名。
                    java 中：  参数类型 参数名
                    scala中：  参数名 ： 参数类型

             Array[String]：  Array是类，在java中使用[]代表数组，并不复合面向对象的特征！
                            在scala中，数组应该有一个类型！

                            [String]: 数组中的类型是String！


          3. : Unit
                　: 后跟的是函数或方法的返回值类型！
                  Unit类型通常用来表示无返回值的函数！类似java中的void!

             = : 为了体现函数或方法的地位，访问的统一性！
                    在java中，给一个变量赋值　　　变量类型　变量名　＝　值
                    在java中，方法必须写在　class中　　　方法签名　{ 方法体　}
                    方法必须依赖于Ｃlass存在！

                   在scala中，函数必须有拍面！函数作为一等公民！

           ４.　语句的结束
                  在scala中，本着简洁原则，通常语句结束后，如果换行，可以省略;
                   在一行中，编写了多个语句，需要使用;尽量逻辑的分割

            5. scala默认会为所有的 xxx.scala源文件，自动导入 Predef._ (导入Predef对象中的所有内容)

                当前对象中有和导入的方法重名的方法，采取就近原则，使用自己的方法！


              println("hello scala!")本质和System.out.println()是一样的！


              import xxx.Predef._







   */
  def main(args: Array[String]): Unit = {

    //System.out.println("hello java")

    println("hello scala!")

    //Predef.println("hello scala!")

  /*  Hello.hello()  ;  Hello.hello()

    Hello.hello()
    Hello.hello()
    Hello.hello()*/

    //读取一行内容
    var str= StdIn.readLine()

    // 获取一个Int类型
    val i: Int = StdIn.readInt()

    // 获取一个Double类型
    val d: Double = StdIn.readDouble()

    println(str + i + d)


  }

  /*def println(): Unit = {

    System.out.println("HelloScala 自己的!")

  }*/



}
