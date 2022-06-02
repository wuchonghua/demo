package com.bigdata.spark.core

import com.example.demo.Test1
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author wuchonghua
 * @create 2022-05-23 14:06
 */
object TestSeril {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val rdd = sc.parallelize(List(1, 2, 3, 4))
    Test1.init("123", "abc")
    rdd.map(
      x => {
        println(Test1.m)
        x+1
      }
    ).take(2).foreach(println)
    Test1.init("1234", "abcd")
    rdd.map(
      x => {
        println(Test1.m)
        x+1
      }
    ).take(2).foreach(println)
    sc.stop()
  }

}
