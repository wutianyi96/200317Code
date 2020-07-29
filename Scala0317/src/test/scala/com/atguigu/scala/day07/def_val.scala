package com.atguigu.scala.day07

import org.junit.Test

/**
  * 描述：    => 符号的使用  ； def 和 val 的区分
  *
  * @author WuTianyi
  * @date 2020/7/6
  */
class def_val {

    @Test
    def test1() : Unit ={

      //  =>符号使用场景
/*      //定义一个方法
      def m() = x + 3
      def m(x: Int) = x + 3
      //定义一个函数变量
      var x:(Int) => Int = x
      //定义一个函数
      val f = (x:Int) => x

      //匿名函数
      val f1 = (x:Int) => x + 1*/


      }

    @Test
    def test2() : Unit ={

      //def和val区别：

      //默认情况的函数调用的参数为call-by-value
      def callByValue(x: Int) = {
        println("x1=" + x)
        println("x2=" + x)
        //这种情况，func会先执行，也就会先打印出hello一次，然后将返回的直接传入作为参数
      }
      //call-by-name
      def callByName(x: =>Int) = {
        println("x1=" + x)
        println("x2=" + x)
        //这种情况，func不会先执行，而是会整个代入（按名字传入）这个函数，在使用到的时候，
        // 也就是两次打印出x的时候再调用，这样就会出现两次hello了
      }

      def func() = {
        //这个函数有副作用，除了返回值还会打印出hello
        println("hello")
        1
      }


//      总结:
//      def 和 val的关系其实就是call-by-name和call-by-value的关系，
//      def对应的是by-name，val对应的是by-value


      callByValue(func())
      println("--------------------")
      callByName(func())
      }

      /*
        运行结果：
        hello
        x1=1
        x2=1
        --------------------
        hello
        x1=1
        hello
        x2=1
       */


}
