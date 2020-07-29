package com.atguigu.scala.day09

import java.io.File

import scala.io.Source

/**
  * Created by VULCAN on 2020/7/8
  */
object ImplicitObject {

  implicit class MyFile1(x:File){

    def printAllLines1() ={
      Source.fromFile(x).getLines().foreach(println)

    }

  }

}
