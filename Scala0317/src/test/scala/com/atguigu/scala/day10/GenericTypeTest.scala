package com.atguigu.scala.day10

/**
  * Created by VULCAN on 2020/7/10
  *      泛型：
  *            泛(Generic)型(Type) :   参数类型化
  *                  作用：  ①在编译时，对声明的泛型类传入参数进行类型校验
  *                          ②省略了一些强转
  */
import  org.junit._
class GenericTypeTest {

  @Test
  def test() : Unit ={

    val l1: List[GrandPa1] = List[GrandPa1]()
    val l2: List[Father1] = List[Father1]()
    val l3: List[Son1] = List[Son1]()
    val l4: List[Other1] = List[Other1]()

    // scala中泛型有不可变性，还可以有逆变和协变其他的特性
    // 不是所有类型都支持逆变和协变
    val l5: List[Any] = List[Other1]()

    val genericTest = new GenericTest4()

    // 和java上限一致
    //genericTest.test1(l1)    //和java上限一致，最多是Father类型
    /* genericTest.test1(l2)
     genericTest.test1(l3)*/
    //genericTest.test1(l4)   // Other1和Father1没有继承关系

    // 无下限，随便传  前提是当前类型支持协变  类型[+T]
    // val l5: List[Any] = List[Other1]()

    genericTest.test2(l1)
    genericTest.test2(l2)
    genericTest.test2(l3)
    genericTest.test2(l4)

  }

  @Test
  def test2() : Unit ={

    // 协变和逆变

    // 泛型的不可变型
    val o1 : Other2[GrandPa1] = new Other2[GrandPa1]

    // not ok
    /*val o2 : Other2[GrandPa1] = new Other2[Father1]
     val o2 : Other2[GrandPa1] = new Other2[Other1]*/


    //  类名[+T] : 协变   ，允许将泛型的子类赋值泛型的父类
    val o3 : Other3[GrandPa1] = new Other3[GrandPa1]
    val o4 : Other3[GrandPa1] = new Other3[Father1]

    //  名[-T] :逆变： 允许将泛型的父类赋值泛型的子类
    val o5 : Other4[Son1] = new Other4[Father1]
    // 协变和逆变只能选其一



  }

  // 泛型的上下文
  @Test
  def test3() : Unit ={

    // 提供一个比较两个不同类型数据大小的方法
    //  Scala已经提供了常用AnyVal类型的Ordering隐式变量实现
    def myCompare[T](a:T,b:T)(implicit ord: Ordering[T])={

      if (ord.lt(a,b)) -1
      else if(ord.equiv(a,b)) 0
      else 1

    }

    //调用方法
    // println(myCompare(1, 2))
    // println(myCompare(new Other, new Other))

    // 泛型上下文： 一种语法格式，将泛型和隐式参数和隐式转换相结合的一种语法
    // [T :Ordering]: 当前方法也需要一个 implict Ordering[T]
    def myCompare1[T :Ordering](a:T,b:T)={

      //隐式召唤
      val ord: Ordering[T] = implicitly[Ordering[T]]

      if (ord.lt(a,b)) -1
      else if(ord.equiv(a,b)) 0
      else 1

    }

    myCompare1(11.0,11.0)


  }

}

class GenericTest4 {
  // 传入的必须是Father类型  泛型的上限
  def test1[T <: Father1](list: List[T]): Unit = {
  }

  // 传入的必须是Father继承的类型  泛型的下限 至少需要一个Father
  def test2[T >: Father1](list: List[T]): Unit = {
  }
}

class GrandPa1 {}

class Father1 extends GrandPa1 {}

class Son1 extends Father1 {}

class Other1 {}

class Other2[T]{}
class Other3[+T]{}
class Other4[-T]{}
