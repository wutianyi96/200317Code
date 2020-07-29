package com{

  import com.atguigu.scala1.day05.PackageTest

  class Cat{

      def hello: Unit ={

        new PackageTest

      }

  }

  package atguigu{

    package scala1{

      import  com.atguigu.scala1.day05

      class Cat1{

        println(day05.i)
        day05.hello

      }

      package day05{

        class Cat{

          def hello: Unit ={

            new PackageTest



            println(i)

          }

        }

        /*object Cat{

        }

        trait Cat1{

        }*/

       /* val i = 1

        def sayHello(){

      }*/


        import com.Cat
        import org.junit._
        class PackageTest {

          @Test
          def test() : Unit ={

            new com.Cat

          }

          @Test
          def test1() : Unit ={

             hello

            println(i)

          }

        }

      }

    }

  }


}







/**
 * Created by VULCAN on 2020/7/3
 *
 *    package:
 *                包的作用:
 *                            ①管理类的源文件
 *                            ②方便归类查找外，包可以区分同名的类
 *                            ③包可以将编译后的字节码文件按照包的路径放入类路径下
 *                            ④控制访问权限
 *
 *                包的本质：
 *                            将编写的类的源文件，编译打包后放入指定的类路径
 *
 *                 语法：      package  包名
 *
 *
 *            和类相关的文件：
 *                      java ：   xxxx.java     xxx.class
 *
 *                      源文件：    com\atguigu\scala\day04
 *                      包路径：    com.atguigu.scala.day04
 *                      字节码文件：com\atguigu\scala\day04
 *
 *                      限制： ①包声明的路径必须和源文件存放的磁盘的包路径一致
 *                            ②java会将源文件和字节码文件都让如对应的包路径下
 *
 *
 *
 *
 *                      scala :   xxx.scala    xxx.class
 *
 *                      源文件： com\atguigu\scala\day05
 *                      包路径：  com.atguigu.scala1.day05
 *                      字节码：  com\atguigu\scala1\day05
 *
 *                      允许源文件存放的路径和声明的包路径不一致！
 *                      将字节码存放在包声明的路径下！
 *
 *
 *              包的命名：
 *                        不能以数字开头！
 *                         不能使用scala已经定义的关键字
 *
 *                         包的命名需要有意义：
 *                              公司、组织的域名反写 + 项目名 + 模块(module) + 类型
 *
 *                            www.atguigu.com      com.atguigu
 *                            pay.atguigu.com
 *
 *
 *
 *              包的细节：
 *                  java  :  一个源文件中packge只能声明一次！
 *                            包名也不能进行拆分！
 *
 *                  scala :   ①package可以声明多次！
 *                            ②对包名进行拆分，拆分为上下级的子父结构！
 *                            ③允许使用{} 来显式声明包的范围
 *                            ④ 在子包中可以访问父包中定义的内容
 *                            ⑤ 在父包中，不能访问子包中定义的内容！ 需要导包！
 *                            ⑥ 如果父包和子包中有同名的类，默认子包根据就近原则！
 *                                可以使用完成的类路径，明确使用哪个类
 *
 *
 *              包也是个对象：
 *                  在包里可以声明 class, object ,trait
 *                  不能写属性和方法！
 *
 *              包对象： 可以创建一个包对象，将希望在包中写的属性和方法进行声明！
 *                        一个包只能创建一个包对象！
 *                        包对象的名称必须和包名一致！
 *                        包对象中定义的属性和方法可以在包所定义的类中直接使用！
 *
 */


