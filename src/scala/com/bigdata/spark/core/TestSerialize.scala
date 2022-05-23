package com.bigdata.spark.core

import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
 * @author wuchonghua
 * @create 2021-12-16 13:49
 */
class A {

   val a = 2
  def work(i : Int): Int ={
    i + 2
  }
}

object TestSerialize {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(List(1, 2, 3, 4))
    val a = new A()
    rdd.map(
      a.work
    ).take(2).foreach(println)
    sc.stop()
  }
}

