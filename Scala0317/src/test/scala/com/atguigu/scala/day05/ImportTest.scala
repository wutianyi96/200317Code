package com.atguigu.scala.day05

/**
 * Created by VULCAN on 2020/7/3
 *
 *    import :
 *            默认导入的包：
 *                  java:    java.lang.*
 *
 *                  scala:    java.lang.*
 *                            Predef.scala中的内容
 *                            scala包下的内容例如Int，不能使用子包（Stdin）
 *
 *            手动导入包：
 *                  java：  import必须在源文件头部声明！
 *                  scala中：  在任意位置声明！可以通过声明的位置，来指定导入类的使用范围！
 *
 *             导入类的作用域：  import语句最接近的外部的{}
 *
 *             导入所有:   import  xxx.xx.xx.*
 *                        import xxx.xxx.xx._
 *
 *              选择器 :    只选择导入包中的指定类
 *
 *              屏蔽: java.util.{Date => _}
 *
 *                    import java.util.{Date => _ , _}  导入除了Date的其他类
 *
 *              起别名:   type utilDate = java.util.Date
 *                         import java.util.{Date => UtilDate , _}
 *
 *               根引入： 默认import是优先从相对路径引入，相对于当前源文件所在的包路径进行引入
 *
 *                        从根路径(项目)导入：  使用 _root_
 *
 *
 */

// 选择器
//import scala.collection.mutable.{HashMap, HashSet}

// 屏蔽HashMap
import scala.collection.mutable.{HashMap => _, HashSet}
import scala.io.StdIn

// 屏蔽
/*import java.sql.Date
import java.util.{Date => _ , _}*/

//重命名
//import java.sql.Date

//import java.util.{Date => UtilDate , _}

class ImportTest {

  import org.junit._

  @Test
  def test() : Unit ={


    val i : Int = 10

    StdIn

 /*   new HashMap[]()

    new HashSet[]()*/

    //new Date

   /* type utilDate = java.util.Date
    new utilDate()*/

    //val s : String = "aa"


    import  _root_.java.util.HashMap
    new HashMap

  }

}

package  java{
  package  util{
    class HashMap{

    }
  }
}