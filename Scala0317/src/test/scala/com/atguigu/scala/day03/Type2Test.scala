package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 *
 *    介绍 Unit ， Null ，Nothing
 */

import org.junit._

class Type2Test {


  /*
      Unit ：  模拟java中没有返回值的场景！
                有返回值!
                返回的是一个Unit类型（没返回值）的返回值！

                只有一个实例  ()

             Unit 的本质是为了改造Java中的void（不是面向对象）

        在scala中任意的方法或 代码块 执行后都有返回值！
   */
  @Test
  def test1() : Unit ={

       val o = new Type2Test()
    val res: Unit = o.testUnit()

    println(res)  //()

  }

  /*
        null : 空值，这个变量声明了，指定一个空的地址值！
                不符合完全面向对象！

        Null : 只有一个实例 null
            Null存在的意义就是为了改造java中的null

   */
  @Test
  def test2() : Unit ={

    val i : Null = null

    val s : String = i

    // Null类型不能赋值给AnyVal类型
    //val j : Int = i    //not ok

    //println(j)


  }

  /**
      Unit: 正常的什么都不返回！  没什么可给你！
      Nothing:  异常的什么都不返回！  没办法给你！

      Nothing :    任意类型在子类！ 可以赋值给任意类型！
          使用场景：  在抛出异常时使用！

        为什么要有Nothing：  ①scala作者认为一切方法的调用，都得有返回值！
                              即便是异常了，也得有返回值！
                               Nothing代表异常的返回情况(没法返回)

        为什么Nothing要作为最底层的类，也就是所有类型的子类：
                纯粹是为了编译器的自动类型推断！
                 只有作为最底层类，才可以在极端情况下，让编译器推断出合适的类型！

   */


  /*
      在scala中不建议使用return来返回结果！
      return 有可能让方法整个结束！

      默认返回代码块的最后一行
   */

  @Test
  def test5() : Unit ={

    val i = 10

    // 希望使用一个类型，可以兼容不同条件的返回值，但是又不希望这个类型过于大！

    val res :Int = if ( i > 0){
      //返回一个整数
      1
      //"hello"
      //println("hello")

    }else{
      throw new Exception("啊偶，异常了~")
    }

    println(res)
  }

  @Test
  def test4() : Unit ={

    val i = 10

    val res = if ( i > 0){
      //返回一个整数
       //1
      "hello"
      //println("hello")

    }else{
       2
    }

    println(res)

  }

  @Test
  def test3() : Unit ={

     val o = new Type2Test()
   // val res: Nothing = o.testNothing()

   // println(res)

  }

  @Test
  def test6() : Unit ={

    //打印 指定类指定方法的返回值类型
    // scala.runtime.Nothing$  代表 Nothing
    println(getClass.getDeclaredMethod("testNothing1").getReturnType.getName)


  }

  //编译器自动推断
  def testNothing1() ={

    throw new Exception("啊偶，异常了~")

  }

  def testNothing(i : Int)  ={

    // xxxxx  一堆逻辑

    if (i > 0){
      throw new Exception("啊偶，异常了~")
    }

    "hello"  //返回Any的任意子类
    // xxxxx

  }


  def testUnit() : Unit ={

    println("testUnit")

  }

}
