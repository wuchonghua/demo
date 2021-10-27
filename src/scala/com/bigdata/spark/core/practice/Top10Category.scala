package com.bigdata.spark.core.practice

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * @author wuchonghua
 * @create 2021-10-03 14:28
 */
object Top10Category {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val lineRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")


    val id2Count1RDD: RDD[(String, (Int, Int, Int))] = lineRDD.flatMap(
      line => {
        val splitwords = line.split("_")
        if (splitwords(6) != "-1") {
          // 点击
          List((splitwords(6), (1, 0, 0)))
        } else if (splitwords(8) != "null") {
          // 下单
          val ids: Array[String] = splitwords(8).split(",")
          ids.map((_, (0, 1, 0)))
        } else if (splitwords(10) != "null") {
          val ids: Array[String] = splitwords(10).split(",")
          ids.map((_, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    val id2CountRDD: RDD[(String, (Int, Int, Int))] = id2Count1RDD.reduceByKey {
      case (a, b) => {
        (a._1 + b._1, a._2 + b._2, a._3 + b._3)
      }
    }
    id2CountRDD.sortBy(_._2, false).take(10).foreach(println)


    sc.stop()
  }

}
