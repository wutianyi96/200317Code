package com.atguigu.scala.day05

/**
 * Created by VULCAN on 2020/7/3
 * 其他特性：
 * ①函数作为返回值
 * ②闭包
 * ③柯里化
 *
 *
 */

import org.junit._

class FunctionOtherFutureTest {

  @Test
  def test1(): Unit = {

    //声明函数
    def fun1(i: Int) = {

      def fun2(j: Int) = {

        j + 1

      }

      // 声明外层函数的返回值,希望返回一个函数的声明
      fun2 _

    }

    //调用函数
    val f1: Int => Int = fun1(1)

    println(f1(1))

    // 一步到位

    println(fun1(1)(1))

    //  fun1()()()()()  :  最后一个()之前返回的都是函数！

  }

  /**
     闭包:
              将函数和函数所处的上下文(环境)的整体称为闭包！

              将fun4函数看作是一个对象，外层使用的i,j,k都是对象的属性！  整体称为叫闭包！

      作用：   有了闭包，函数式编程才更加灵活！

               部分函数依赖于闭包功能！
               柯里化也依赖于闭包！

       10分钟：  声明普通函数，计算3个Int型参数的乘积！
                使用闭包方式，计算3个Int型参数的乘积！
                使用柯里化函数，计算3个Int型参数的乘积！
   */

  @Test
  def test2(): Unit = {

    //定义
      def fun1(i: Int) = {

        def fun2(j: Int) = {

          def fun3(k: Int) = {

            def fun4(l: Int, f: (Int, Int, Int, Int) => Int): Int = f(i, j, k, l)

            fun4 _
          }
          fun3 _
        }
        fun2 _
    }

    // 使用
    //  fun2
    val f1: Int => Int => (Int, (Int, Int, Int, Int) => Int) => Int = fun1(1)

    //fun3
    val f2: Int => (Int, (Int, Int, Int, Int) => Int) => Int = f1(2)

    // fun4
    val f3: (Int, (Int, Int, Int, Int) => Int) => Int = f2(3)

    val res: Int = f3(4, _ + _ + _ + _)

    println(res)

    // 一步到位
    println(fun1(1)(2)(3)(4, _ * _ * _ * _))


  }

  /*
     柯里化：    逻辑学家 ：  Haskell Brooks Curry
                崇尚做事有逻辑！

                我今天起床洗漱完后出门！

                我今天起床
                起床后洗漱
                洗漱完出门

                库里推崇在使用函数式编程时，要将每个功能尽量拆分，有逻辑顺序！


        函数的柯里化： 在声明函数时，将函数的一个参数列表变为多个参数列表！

        有多个参数列表的函数，称为柯里化函数！



   */
  @Test
  def test3() : Unit ={

    //定义
    def fun1(i: Int) = {

      def fun2(j: Int) = {

        def fun3(k: Int) = {

          def fun4(l: Int, f: (Int, Int, Int, Int) => Int): Int = f(i, j, k, l)

          fun4 _
        }
        fun3 _
      }
      fun2 _
    }

    println(fun1(1)(2)(3)(4, _ * _ * _ * _))

    //柯里化
    def fun1Curry(i: Int)(j: Int)(k: Int)(l: Int, f: (Int, Int, Int, Int) => Int) :Int ={
      f(i,j,k,l)
    }

    //调用
    println(fun1Curry(1)(2)(3)(4, _ * _ * _ * _))
  }


  /*
                声明普通函数，计算3个Int型参数的乘积！
                使用闭包方式，计算3个Int型参数的乘积！
                使用柯里化函数，计算3个Int型参数的乘积！
   */
  @Test
  def test4() : Unit ={

    //普通函数
    def mul(i:Int,j:Int,k:Int) = i * j * k

    println(mul(1, 2, 3))

    //闭包：  函数和所处的环境称为闭包！  显著特征，在一个函数中使用了外部的变量！
    def mulClosure(i:Int)={

      def f1(j:Int)={

        def f2(k:Int) : Int= i * j * k
        f2 _
      }
      f1 _
    }

    println(mulClosure(1)(2)(3))

    //柯里化

    def mulCurry(i:Int)(j:Int)(k:Int) = i * j * k

    println(mulCurry(1)(2)(3))


  }

}
