package com.atguigu.scala.day03

/**
 * Created by VULCAN on 2020/6/30
 *
 */
import org.junit._
class OperatorTest {

  /*
     运算符在scala中的本质是方法/函数

     scala中没有 ++，--，scala作者认为 ++,--设计的不好，还很鸡肋！容易让人引起歧义！
        使用 += 1 代替 ++
            -= 1  代替 --
   */

  @Test
  def test1() : Unit ={

    val i = 1
    val j = 2

    println(i * j)
    println(i.*(j))

  }

  @Test
  def test2() : Unit ={

    var i = 1


    i += 1
    i -= 1
    println(i)

  }

  // 假如还有97天放假，问：xx个星期零xx天

  @Test
  def test3() : Unit ={

    val days = 97

    printf("还有 %d 天放假, %d个星期零 %d天",days,days/7,days%7)

  }

  //  华氏温度转换摄氏温度的公式为：5/9*(华氏温度-100),请求出华氏温度对应的摄氏温度。[测试：232.5]
  @Test
  def test4() : Unit ={

    val t = 232.5

    printf("华氏温度 %.2f 对应的摄氏温度: %.2f ",t , 5.0 / 9 * (t-100))


  }

}
