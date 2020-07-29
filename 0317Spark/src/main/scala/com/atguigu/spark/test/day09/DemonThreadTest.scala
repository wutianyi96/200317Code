package com.atguigu.spark.test.day01.day09

/**
  * Created by VULCAN on 2020/7/23
  */
object DemonThreadTest {

  def main(args: Array[String]): Unit = {

    val myThread = new MyThread

    val thread1 = new Thread(myThread, "分线程")

    //设置当前线程为守护线程
    // 如果一个JVM中只有守护线程，虚拟机就关闭！
    thread1.setDaemon(true)

    thread1.start()

    Thread.sleep(5000)

    println(Thread.currentThread().getName+"--->启动了！")


  }

}

class MyThread extends  Runnable{

  override def run(): Unit = {

    println(Thread.currentThread().getName+"--->启动了！")

    for (i <- 1 to 10){

      Thread.sleep(1000)

      println(Thread.currentThread().getName+"--->"+i)

    }

  }
}
