package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
  *
  *      抽象类：  将拥有抽象方法或属性的类，称为抽象类！ 使用abstract修饰！
  *
  *                抽象类中可以有普通方法！
  *                抽象类中可以有普通的属性！
  *
  *      抽象：   不具体
  *
  *            抽象方法： 不具体的方法，只有方法名称，参数列表，没有实际的方法体
  *            抽象属性：  只有属性的名称和类型，没有赋值初始化
  *
  *             抽象方法和属性不能使用abstract修饰，修饰就报错！ 只需要省略方法或属性的赋值即可！
  *
  *
  *      抽象类不能被实例化，需要被子类继承，由子类将抽象的属性和方法，进行重写，具体化！
  *      子类继承了一个抽象类，但是没有重写抽象方法或属性，此时子类也得是抽象类！
  *
  *      抽象方法和属性，不能被final，private修饰。抽象属性和方法存在的意义就是为了被子类重写！
  *
  *      抽象属性和方法的重写，可以不加override!
  *      普通属性和方法的重写，必须加override!
  *
  *      抽象类，也可以声明主构造器！
  *				目的为了子类实例化的时候调用父类声明的构造器！
  *				因为抽象父类也有声明非抽象属性和方法，需要给它们传参数赋值！
  *
  *
  *
  */
import  org.junit._
class AbstractClassTest {

  @Test
  def test() : Unit ={

    val c = new Class3(10)

    c.hello()
    println(c.age)

    /*
        class1主构造器被调用
        hello
        10
     */


  }

}

abstract class  Class1(var i:Int){

  var age : Int = 0

  println("class1主构造被调用")

  age = i

  //抽象属性
  val name : String

  //普通属性


  //抽象方法
  def hello()

  //普通方法
  def hi()={
    println("hi")
  }

}

class Class3(i:Int) extends  Class1(i){

  val name: String = "jack"

  def hello(): Unit = {

    println("hello")

  }
}

abstract class  Class2{

  //普通属性
  val age : Int = 0

  //普通方法
  def hi()={
    println("hi")
  }

}

