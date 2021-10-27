package com.bigdata.spark.core

/**
 * @author wuchonghua
 * @create 2021-10-12 11:15
 */
object TestClosure {

  def main(args: Array[String]): Unit = {
    val f = func(0)
    println("-------------")
    for (i <- 1 to 10) {
      f(i)
    }
  }

  def func(a: Int): Int => Unit ={
    println(a)
    b => println(a + b)
  }

}
