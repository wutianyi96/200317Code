package com.atguigu.scala.day02

/**
 * Created by VULCAN on 2020/6/29
 */
import java.io.PrintWriter

import org.junit._

import scala.io.{BufferedSource, Source, StdIn}

class PrintStrTest {

  /*
       字符串输出
          type String        = java.lang.String
            scala中的String和java中的String是等价的！

            scala认为java中的string不够强大，因此scala提供了隐式转换功能，可以在必要的时候，将java的String
            转为Scala定义的一个增强String类型  StringOps
   */
  @Test
  def test() : Unit ={

     val name : String = "jack"
     val age : Int = 20
     val height : Double = 183.5

    // 字符串拼接
    println("hello: " + name)

    /**
        字符串的格式化输出:
            %s:  必须传入一个字符串
            %d:  必须传入一个整数
            %f:  必须传入一个浮点型数据。
                  默认保留小数点后六位

            %.1f:        保留小数点后1位

         def printf(text: String, xs: Any*) = Console.print(text.format(xs: _*))
              第一个参数必须是一个String
             xs: Any*: 可变数组(数组中什么都可以放) ，类似Java中的 Object...xs


     */

    //不会换行
    printf("name: %s, age : %d ,height : %.1f  \n", name, age, height )
    printf("name: %s, age : %d ,height : %.1f ", name, age, height )

  }


  /*
        插值字符串，在字符串输出的位置预留占位符，解析后，插入指定变量
   */
  @Test
  def test2() : Unit ={

    val name : String = "jack"
    val age : Int = 20
    val height : Double = 183.5

    // s： 代表字符串需要 (插值) 解析
    val info =  s" name: $name , age : ${age.floor * 2}  ,height : $height     "

    println(info)

  }

  /*
      .stripMargin : 将字符串中 | 之前的全删除！
                      |可以作为一个(顶格符号)！

        stripMargin('!') : 自定义指定符号作为顶格符号！
   */
  @Test
  def test3() : Unit ={

    val sql : String =
      """
     !select *
        !from emp
        !where age > 20

      """.stripMargin('!')

    println(sql)
    /*
    select *
    from emp
    where age > 20
     */

  }

  /*
      {"age":$age,"name":$name }
   */
  @Test
  def test4() : Unit ={

    val name : String = "jack"
    val age : Int = 20
    val height : Double = 183.5


    //val myJsonStr = s"{\"age\":$age,\"name\":$name}"  //not ok

    val myJsonStr =
      s"""
        | {"age":$age,"name":$name}
        |""".stripMargin

    println(myJsonStr)
    /*
    {"age":20,"name":jack}
     */
  }
  
  /*
       Stdin : 在mian/scala中测试
   */
  @Test
  def test5() : Unit ={
  
    //读取一行内容
      var str= StdIn.readLine()

    // 获取一个Int类型
    val i: Int = StdIn.readInt()

    // 获取一个Double类型
    val d: Double = StdIn.readDouble()
      
  }

  /*
    读取文件
        相对路径：  当前代码执行时，相对路径以当前module作为父目录！
                    相当于当前module
   */
  @Test
  def test6() : Unit ={

    // 从读文件中获取内容
    val source: BufferedSource = Source.fromFile("input/word.txt")

    //获取一个可以一行一行迭代的 可迭代的集合
    val iterator: Iterator[String] = source.getLines()

    // 迭代
    while(iterator.hasNext){

      println(iterator.next())

    }

    //关闭source
    source.close()

  }

  @Test
  def test7() : Unit ={

    val writer = new PrintWriter("output/myout.txt")

    writer.write("nihao")

    writer.flush()

    writer.close()

  }



}
