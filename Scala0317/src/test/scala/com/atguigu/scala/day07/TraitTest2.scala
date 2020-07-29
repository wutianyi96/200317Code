package com.atguigu.scala.day07

/**
  * Created by VULCAN on 2020/7/6
          叠加特质（有血缘关系，混入某个共同特质）：
            先初始化，再接着执行：

                  ①：叠加特质在初始化时，（从左向右，父类优先），如果有共同父类，父类初始化只进行一次！
                  ②：执行时，（由右向左）！  注意：当特质中，调用super.xx(),代表继续向左调用特质的xx,(不是真正父类)
                  如果左侧没有特质，才代表调用父类的xx()

                  如果希望，super.xx()，不向左继续寻找特质，而是直接调用父类的方法，需要在super后添加[父类]
                  父类只能是直接父类！修改完，不会再向左寻找特质！


  *
  */

import  org.junit._
class TraitTest2 {


  @Test
  def test1() : Unit ={

    val myOperate = new MyOperate with NetWorkOperate with FileOperate

    /*
        修改后
         FileOperate操作了:10
          DBOperate操作了:10


     */
    myOperate.operateData(10)

  }


}
//new MyOperate with NetWorkOperate with FileOperate
/*
operate!  DB!  NetWorkOperate! FileOperate!
 */
trait  Operate{
  println("operate!") ;   def operateData(i:Int)}

trait  DBOperate extends Operate {
  println("DB!") ; override def operateData(i: Int): Unit = println("DBOperate操作了:" + i)}

trait  FileOperate extends DBOperate {
  println("FileOperate!")
  override def operateData(i: Int): Unit = {
    println("FileOperate操作了:" + i)

    // 默认向左继续寻找特质
    // super.operateData(i)

    // 直接调用父类方法
    super[DBOperate].operateData(i)

    //super.operateData(i)
  }
}

trait  NetWorkOperate extends DBOperate {
  println("NetWorkOperate!")
  override def operateData(i: Int): Unit = {
    println("NetWorkOperate操作了:" + i)
    super.operateData(i)
  }
}

class MyOperate {}






