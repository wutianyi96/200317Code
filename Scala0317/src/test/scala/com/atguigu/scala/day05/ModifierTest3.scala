package com.atguigu.scala.day05

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
  *
  *                避开坑： 子父类尽量放入一个包中！
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
  *
  */
import com.atguigu.scala.day06.MyClass2
import org.junit._

class ModifierTest extends MyClass2 {

  @Test
  def test1() : Unit ={

    val myClass = new MyClass2

    /*myClass.address

    myClass.hello2()
*/
  }



}


