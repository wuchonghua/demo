package com.bigdata.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author wuchonghua
 * @create 2021-12-15 18:07
 */
object RDDAddOutVar {


  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    var counter = 0
    val rdd = sc.parallelize(List(1, 2, 3, 4))

    // Wrong: Don't do this!!
    rdd.foreach(x => counter += x)

    println("Counter value: " + counter)
    sc.stop()
  }

}
