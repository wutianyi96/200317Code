package com.atguigu.scala.day05

/**
 * Created by VULCAN on 2020/7/3
 *
 *    递归 : 让代码简洁！
 *
 *    不是所有的场景都可以使用递归！
 *          ①在某些业务中，压根不需要，或无法使用递归！
 *          ②在某些场景下，递归可能会造成栈顶溢出    StackOverFlowError
 *              求斐波那契数值，一旦求斐波那契数非常大，递归会呈指数级别增长！
 *
 *     递归的注意事项：
 *            ① 把握计算的规律
 *            ② 递归需要使用退出条件来结合递归
 *            ③ 每次递归，程序必须无限接近退出条件
 *
 * 阶乘
 * 范围求和
 * 反转字符串
 * 求集合最大值
 *
 *
 *      尾递归：  在递归方法中，递归调用时，有且只有递归方法参与运算，此时称为尾递归！
 *                尾递归好！ 同一时刻，栈中只有一帧！
 *                不会造成StackOverFlowError！
 *
 *                某些情况，可以将递归优化为尾递归，但是不所有的情况！
 *
 *      发现是否是尾递归：  ①看IDEA的提示
 *                        ②@tailrec 测试是否是尾递归！
 *
 *
 *
 *
 */

import com.sun.deploy.util.StringUtils
import org.junit._

import scala.annotation.tailrec

class RecursiveTest {

  @Test
  def test1() : Unit ={

    //阶乘
   // @tailrec
    def jiecheng(i : Int): Int ={

      //退出条件
      if ( i <= 1) 1
      else i * jiecheng(i - 1)
    }

    // 测试
    println(jiecheng(5))

  }

  @Test
  def test2() : Unit ={

    //范围求和   求： 1-100之间的和

    // 常规
    var result = 0

    for (i <- 1 to 100){
      result =result + i
      //i+=1
    }

    println(result)

    // 分析规律
    // 声明一个两个Int值计算的函数
    // 是一个尾递归方法！
    @tailrec
    def sumRecursive(i:Int,result:Int) : Int={
      // 结束条件
      if ( i>100 ) result
        // 将i 累加到result,i+1
      else sumRecursive(i+1,result+i)
    }

    println(sumRecursive(1, 0))

  }

  /**
        反转字符串
            字符串理解是字符的集合！
               集合.tail :   非头即尾，只要不是head，剩下的都是tail
               集合.head ：  返回集合中的第一个元素
   */

  @Test
  def test3() : Unit ={

    val list = List(1, 2, 3, 4)

    println(list.head)  // 1
    println(list.tail)  // List(2, 3, 4)


    val s : String ="abcdefg"
    println(s.head)  // a
    println(s.tail)  // bcdefg

    def stringReverse(s : String) : String ={

      //先判空
//      if (s == null || s == "") throw new java.util.NoSuchElementException()

      // 如果只有一个字符
      if (s.length ==1) s
      // 反转    头部(head) 后置 拼接 (头部后置的 剩下的字符串)
      /*
            1.  参数：abcdefg   返回   stringReverse(bcdeft) +  a
            2.  参数： bcdeft    返回   stringReverse(cdeft) + b
                  ...
            n.   参数： g   返回 g
            bcdefg    a
         */
      else  stringReverse(s.tail)+ s.head

    }

    println(stringReverse(s))


  }

  /*
      求集合最大值
   */
  @Test
  def test4() : Unit ={

    val list = List(1, 2, 30, 4,10,-1)

    // 求最大值
    /*
        第一次：  参数： (1, 2, 3, 4,10,-1)    返回

        第二次：  参数： ( 2, 3, 4,10,-1) 返回

          ....


     */
    def maxRecursive(list : List[Int]) : Int={

//      if (list == null || list.size ==0) throw  new java.util.NoSuchElementException()
      //递归退出条件
      if (list.size == 1) list.head
      else if ( list.head > maxRecursive(list.tail) ) list.head else maxRecursive(list.tail)
    }

    println(maxRecursive(list))


  }

}
