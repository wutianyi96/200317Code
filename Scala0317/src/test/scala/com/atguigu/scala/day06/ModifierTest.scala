package com.atguigu.scala.day06

/**
  * Created by VULCAN on 2020/7/4
  *    Java :
  *            有小到大：  private  default  protected public
  *
  *
  *            private:        本类
  *
  *            default(缺省)：  本类    同包
  *
  *            protected(受保护的) ：本类  同包   子类
  *
  *                子类要不要求和父类同包？
  *                    子类不要求必须和父类同包，子类可以调用继承自父类中的方法！
  *
  *                 ① 同包一定可以访问
  *                 ② 如果子类和父类不同包，子类的实例可以调用继承的父类中的方法！
  *                    子类实例不能直接调用父类中的protected修饰的方法！
  *
  *
  *                避开坑： 子父类尽量放入一个包中！
  *
  *                只有 public 和default可以修饰类！
  *                在一个源文件中，只有有一个public 修饰的类！
  *
  *
  *            public ：  任意位置
  *
  *    Scala :   private  default  protected public
  *
  *        private :  本类中
  *
  *        protected：  本类，子类
  *
  *        default(缺省)： 默认就是公开的
  *
  *        public ： 没有public,写上就报错
  *
  *
  *     如果父类中定义了一个private修饰的属性，子类继承后会拥有！  子类无法使用！
  *      如果父类中定义了一个protectd修饰的属性,子类继承后会拥有！子类可以使用！ 直接用！
  *
  *
  *    不管是否是同包：
  *      子类实例无法直接访问父类实例中定义的protected修饰方法的和属性,
  *      只能访问自己继承的父类中的protected修饰方法的和属性
  *
  *
  *
  */
import  org.junit._

class ModifierTest    {

  @Test
  def test1() : Unit ={

    /* val myClass = new MyClass2

     myClass.name

     myClass.hello()

     //私有不行
     myClass.address

     myClass.hello2()*/

    val myClass3 = new MyClass3

    myClass3.hi()

    // myClass3.hello2()

    val test = new ModifierTest

    test.hi()

  }

  @Test
  def test2() : Unit ={

    val myClass = new MyClass3

    myClass.hi()

  }

  def hi(): Unit ={

    /* address  // 类似于super.address
     hello2()*/

  }



}

class MyClass2{

  val name ="jack"

  def hello()={
    println("hello")
  }

  private val age =11

  private def hello1()={
    println("hello1")
  }

  protected val address ="11"

  protected def hello2()={
    println("hello2")
  }


}

class MyClass3 extends  MyClass2{

  def hi(): Unit ={

    // 当前调用的是父类中 protected修饰的变量和方法，子类可以访问
    address

    // hello2()

    // 同包不能访问！ 子类是需要继承后使用父类中protected修饰的属性
    /* val myClass = new MyClass2

     myClass.hello2()*/

  }

}
