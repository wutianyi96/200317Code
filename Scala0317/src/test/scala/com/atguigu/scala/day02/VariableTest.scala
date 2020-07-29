package com.atguigu.scala.day02

/**
 * Created by VULCAN on 2020/6/29
 */

import com.atguigu.scala.day03
import org.junit._

/**
 * Created by VULCAN on 2020/6/29
 * 文档注释： 写在方法和类上面
 */
class VariableTest {

  @Test
  def test1() : Unit ={

    // 单行注释

    /*
        多行
     */

  }

  //变量
  @Test
  def test2() : Unit ={

      /**
          变量的定义：  val | var  变量名  [: 变量类型] = 变量值
              ①[: 变量类型] 可以省略，编译器由变量值推断变量的类型
              ② 变量一旦声明，必须赋值初始化
              ③ Scala是强类型语言，变量的类型一旦声明，不可修改
              ④ 变量是在方法中声明，方法在栈中运行，一旦方法结束，栈空间释放，变量也随之回收！
                  变量之前不写权限修饰符！
       */

      val i : Int = 1
      var j : Int = 2

      /*
       省略类型声明，由编译器推导
          如何知道类型？
              ①将鼠标指向变量名，IDEA给出提示
              ②使用 isInstanceof[类型] 查看是否是某个类型
                  类似 java  instanceOf

       */
      val i1  = 1

      println(i1.isInstanceOf[Int]) //true

      var j1  = 2.3  //Double

    /*
         变量一旦声明，必须赋值初始化
     */

    // 错误写法
     //var i2 =


    /*
        强类型语言，变量的类型一旦确定，不可修改
     */

     // j1 = "hello "

  }

  /**
        可变变量 和 不可变变量
        var 用于修饰一个 可变变量
        val 用于修改一个不可变变量。 不可变变量，代表变量的值(内存地址)不可变，但是内容可以变！
   */
  @Test
  def test3() : Unit ={

      var i = 1
       i = 2

     val j = 1
    // Reassignment to val  不允许重复赋值给val
      //j = 2

  }

  /**
       不可变变量，代表变量的值(内存地址)不可变，但是内容可以变！

       深刻理解： 可变和不可变
                  在scala中年有可变变量和不可变变量
                  还有可变集合和不可变集合！

        var 和 val 的使用 :   scala作者推荐我们多使用val

                推荐val的原因： 变量大量的使用场景是声明变量后，对变量岁指定的对象的内容进行使用！
                      Person p = new Person()
                      p.getName()
                      p.setName()

                      Person p = new Person()
                       p = new Person()
                        p = new Person()
                         p = new Person()
                  val修饰的变量是只读，在多线程并发访问的情况下，省略线程安全问题！

                  只有需要给变量进行修改时，可以声明为var!
   */
  @Test
  def test4() : Unit ={

    var dog1 = new day03.Dog()

    dog1 = new day03.Dog()

    val dog2 = new day03.Dog()
    // dog2.getName()
  /*  println(dog2.name)

    // dog2.setName("大黄")
    dog2.name="大黄"
    println(dog2.name)
    //dog2 = new Dog()*/



  }

  /*
      标识符：  凡是自己可以命名的地方都称为标识符！
          ①不能以数字开头
          ②可以使用 一些运算符作为标识符 +，-，/
              运算符后，不能跟字母,可以再跟一个运算符
              类似运算符的标识符，不能写在其他标识符的中间或后面

          ③ 很多符号在scala中都可以作为标识符, 一个符号不行，就再拼一个
          ④ 类似Int这种是scala预定义的标识符，可以使用，但是最好不要
          ⑤  使用 ` 随便写 `

          起有意义的名字！
   */
  @Test
  def test5() : Unit ={

    //var 9i = 10 not ok

    var + = 10

    var ++ = 20

    var ! = 20
    var @@ = 20
    var ## = 20
    var $ = 20
    var % = 20
    var ^ = 20

    var `     def      fawefaf   fawefawf ea` = 30

    println(`     def      fawefaf   fawefawf ea`)

    //  Int 是scala已经预定义的一个标识符，可以使用
    var Int = 50

    //var +a = 10  not ok

    //var a+b = 10  not ok
   // var ab+ = 10  not ok

    var - = 10

  }

  // 在很多方法中，形参的类型默认就是val
  def sayHello( name : String) : Unit={

    // name = "haha"

  }

}

// 声明一个自定义类
class Dog{
  //提供一个属性  等同于private String name = "旺财"
  var name = "旺财"


}
