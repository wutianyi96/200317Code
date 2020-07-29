package com.atguigu.scala.day05

/**
 * Created by VULCAN on 2020/7/3
 *    控制抽象：   Breaks.Breakable
 *                     def breakable(op: => Unit){
                         * try {
                         * op
                         * } catch {
                         * case ex: BreakControl =>
                         * if (ex ne breakException) throw ex
                         * }
 *
 *                     }
 *
 *                   函数接受的类型：  => Unit , 代表一段代码！
 *
 *                   将接受一段代码，且没有返回值的函数称为控制抽象！
 *
 *
 *       作用： 让函数式编程更灵活！  直接将一段业务逻辑作为参数传入给函数！
 *
 *       控制抽象在封装框架时，经常使用！将通用的代码抽取为模版，用户自定义的逻辑传入模版中运行！
 *
 *    惰性求值:
 *        java
 *                Emp {                         Dept{
 *                                                 private String deptid
 *                                                 private String location
 *
 *                                                    }
 *                  private String name;
 *                  private Int id;
 *                  private Int age;
 *
 *                  private Dept dept
 *
 *
 *                }
 *
 *         存入到Mysql
 *         emp                             dept
 *         id   age  name  deptId           id  location
 *
 *         将Java对象中的属性值，写入到Mysql对应的表中！
 *         将Mysql表中的每个字段赋值给Java对象中的属性！
 *              称为ORM(object relational mapping)映射
 *
 *
 *           Employee emp = getEmp(1);  // 底层是JDBC， select * from emp where id = ?
 *
 *
 *        问题：  如果需要给 Employee emp 的 dept属性赋值：
 *                  sql ：  select * from emp join dept on emp.deptId = dept.id
 *
 *        场景：   在后续的程序中，只使用了emp对象 id,name,age属性，压根不用dept属性！
 *
 *                 在后续的程序中，又希望偶尔使用下 dept属性！
 *
 *         解决：    当调用  getEmp() 获取Employee emp对象时，只查询emp表select * from emp where id = ?
 *                  一旦程序中，执行  emp.getDept,一旦要使用emp的dept属性，在使用时，
 *                    再临时发送    select * from  dept where id = deptiD
 *
 *          命名 :  延迟查询 或懒加载
 *
 *          在scala中，称为惰性求值！
 *                惰性求值，使用lazy修饰  val类型的变量！
 *
 *
 *
 */

import org.junit._

import scala.util.control.Breaks
class LazyComputeTest {

  @Test
  def test1() : Unit ={

       Breaks.breakable()

  }

  /*
        将创建线程封装为函数！
        将线程运行的逻辑，作为参数吗，传入到函数中！


   */
  @Test
  def test2() : Unit ={

    def createAThread(op: => Unit): Unit ={
      //创建线程
      new Thread(new Runnable {

        //重写run()
        override def run(): Unit = {

          //执行外部传入的逻辑
          op

        }
      }).start()

    }

    //使用
    createAThread({

      Thread.sleep(3000)

      println("三秒真男人")

    })


    Thread.sleep(5000)

  }

  /*
     控制抽象练习

          while( 判断条件 ){
              循环体
              变量的自增等运算
          }

      模仿实现while

      def while(判断条件)(函数体)={}



   */

  @Test
  def test3() : Unit ={

    var i = 0
    while ( i < 5){

      println(i)
      i+=1
    }

    println("-----------------------")

    //声明
    def myWhile(op1 : =>Boolean)(op2 : =>Unit):Any  ={

      //条件为true时，进行循环体的运算
      if (op1) {
        op2
        myWhile(op1)(op2)
      }
    }

    //调用
    var j = 0
    myWhile{j < 5}{
      println(j)
      j+=1
    }


  }

  @Test
  def test4() : Unit ={

    def sayHello(name:String): String ={

      println("hello:" + name)

      "hello:"+name
    }

    //调用
    lazy val str: String = sayHello("jack")

    // 惰性求职
    lazy val str1 = 20

    // .......间隔了1w行代码

    println(str)

  }

}
