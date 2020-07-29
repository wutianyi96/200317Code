package com.atguigu.scala.day07



import  org.junit._
class TraitTest1 {

  @Test
  def test() : Unit ={

    /*
        当一个类继承或混入多个特质，此时按照声明的顺序，从左向右进行实例化！
            如果遇到父类也混入多个特质，父类也遵循由继承的顺序从左向右进行实例化！

            从左向右，父类优先

            t11
            t21
            c12
            t33
            C13
     */
    val c = new C13

  }
}

trait t11{ println("t11") }
trait t21{ println("t21") }
trait t33{ println("t33") }

class C12 extends t11 with t21 {  println("C12")  }

class C13 extends C12 with t33 {
  println("C13")
}




