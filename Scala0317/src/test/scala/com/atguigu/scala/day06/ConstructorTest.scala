package com.atguigu.scala.day06

/**
  * Created by VULCAN on 2020/7/4
  *
  *    构造器也成为构造方法！
  *        在java ：
  *                  声明时要求
  *                      ①构造器的名称必须和类名一致
  *                      ②构造器不能有返回值，void也不可以
  *                      ③在构造器的首行，默认会调用super()
  *                      ④如果类没用提供空参构造器，且没有声明有参构造器，系统会默认提供无参构造器
  *
  *         在scala中：  在函数式编程中，一切皆函数！
  *                      构造器可以使用函数来完成！  类也是一个函数！
  *
  *                      class Bird{}  看作  def Bird(){ 函数体 }
  *
  *                      class Bird{
  *
  *                      // 构造的代码
  *
  *                      }
  *
  *         构造器分类：
  *                 java:   无参构造器  有参构造器
  *                            在一个构造器中，要调用另一个构造器：  this(参数)
  *                                                              super(参数)
  *
  *                 scala:  主构造器    辅助构造器
  *
  *                 主构造器：   class 类名(参数列表) {  构造代码   }
  *                            一个类只能有一个
  *
  *                  辅助构造器：  名称都是this
  *                              参数列表和主构造器要区分！
  *                              在辅助构造器中，必须直接或间接地调用主构造器！
  *                              辅助构造器可以有多个！
  *
  *          在辅助构造器中，必须直接或间接地调用主构造器的原因：
  *
  *                  java 中在构造器的首行，默认会调用super()
  *                  scala中，不支持super(),辅助构造器没有能力执行父类构造器初始化，
  *						   	 必须借助主构造器！

  *                  在scala中，子类如果有父类，先执行父类的构造！
  *                  在scala中，主构造器，默认就指定父类的主构造器！
  *                      类似于 主构造器首行，默认会调用super()
  *
  *
  *          子父类构造的调用顺序：   先调用父类主构造器
  *                                 [父类辅助构造器]
  *                                 子类的主构造器
  *                                 [子类辅助构造器]
  *
  *
  *
  *
  *
  */

import  org.junit._
class ConstructorTest {

  @Test
  def test() : Unit ={

    new Bird("鸭子","灰色","雌")
    //new Bird().Bird("乌鸦")

    //new Bird("乌鸦")


  }

  @Test
  def test2() : Unit ={
    //通过辅助构造器构造对象
    val bird = new Bird("小鸟")


  }

  @Test
  def test3() : Unit ={

    val bird = new Bird("鸡", "黑色")

  }
}


class Bird(name:String,color:String,sex:String) extends  Animal(name,color,sex) {

  //构造的代码
  println("bird 被构造了！")

  //这是普通函数
  def Bird(name : String)={
    println(name)
  }

  def this(name : String)={
    //直接调用主构造器
    // 必须在第一行就调用
    this(name,"白色","雄")
    println(name+"参数列表为name : String的辅助构造器被调用了！")

    //super()

  }

  def this(name : String,color:String)={
    //间接调用主构造器
    // 必须在第一行就调用
    this(name)
    println(name+"参数列表为name : String,color:String的辅助构造器被调用了！")

  }


}

class Animal(){

  println("animal")

  def this(name : String,color:String,sex:String)={

    this()
    println(name+"父类的辅助构造器被调用了！")

  }

}
