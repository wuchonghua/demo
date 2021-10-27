package com.bigdata.spark.core

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author wuchonghua
 * @create 2021-09-07 12:32
 */
object WordCount {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("word-count")
    val sc = new SparkContext(sparkConf)

    val txt = sc.textFile("datas/*")
    val res = txt.flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .collect()
//    val res = txt.flatMap(_.split(" "))
//      .groupBy(x => x)
//      .map{case(word, wordItr) => (word, wordItr.size)}
//      .collect()
    println(res)
    sc.stop()

  }
}
