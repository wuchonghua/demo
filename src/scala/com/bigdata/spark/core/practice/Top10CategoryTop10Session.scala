package com.bigdata.spark.core.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author wuchonghua
 * @create 2021-10-03 14:28
 */
object Top10CategoryTop10Session {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    sc.setCheckpointDir("datas/datas1")
    val lineRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")
    // 一行数据转成一个action对象
    val userVisitActionRDD = lineRDD.map(
      line => {
        val splitwords = line.split("_")
        val userVisitAction = new UserVisitAction(
          date = splitwords(0),
          user_id = splitwords(1).toLong,
          session_id = splitwords(2),
          page_id = splitwords(3).toLong,
          action_time = splitwords(4),
          search_keyword = splitwords(5),
          click_category_id = splitwords(6) toLong,
          click_product_id = splitwords(7) toLong,
          order_category_ids = splitwords(8),
          order_product_ids = splitwords(9),
          pay_category_ids = splitwords(10),
          pay_product_ids = splitwords(11),
          city_id = splitwords(12) toLong
        )
        userVisitAction
      }
    )

    //userVisitActionRDD.cache()
    userVisitActionRDD.checkpoint()


    // 一个action对象转换成点击下单支付数量  一次可以下单支付多个品类 所以flatMap
    val categoryId2Count1RDD: RDD[(String, (Int, Int, Int))] = userVisitActionRDD.flatMap(
      userVisitAction => {
        if (userVisitAction.click_category_id != -1) {
          // 点击
          List((userVisitAction.click_category_id.toString, (1, 0, 0)))
        } else if (userVisitAction.order_category_ids != "null") {
          // 下单
          val orderCategoryIdArray: Array[String] = userVisitAction.order_category_ids.split(",")
          orderCategoryIdArray.map((_, (0, 1, 0)))
        } else if (userVisitAction.pay_category_ids != "null") {
          val payCategoryIdArray: Array[String] = userVisitAction.pay_category_ids.split(",")
          payCategoryIdArray.map((_, (0, 0, 1)))
        } else {
          Nil
        }
      }
    )
    // 聚合数量 得到top10的品类
    val id2CountRDD: RDD[(String, (Int, Int, Int))] = categoryId2Count1RDD.reduceByKey {
      case (a, b) => {
        (a._1 + b._1, a._2 + b._2, a._3 + b._3)
      }
    }
    val top10CategorySet: Set[String] = id2CountRDD.sortBy(_._2, false).take(10).map(_._1).toSet

    // 过滤出top10品类的点击  并统计各品类各session的数量
    val categorySession2CountRDD: RDD[((Long, String), Int)] = userVisitActionRDD.filter(
      userVisitAction => {
        userVisitAction.click_category_id != -1 && top10CategorySet.contains(userVisitAction.click_category_id.toString)
      }
    ).map(
      userVisitAction => ((userVisitAction.click_category_id, userVisitAction.session_id), 1)
    ).reduceByKey(_ + _)
    // 转换成各品类中session的统计数量，分组
    val category2SessionCountRDD: RDD[(Long, Iterable[(String, Int)])] = categorySession2CountRDD.map {
      case ((categoryId, sessionId), count) => {
        (categoryId, (sessionId, count))
      }
    }.groupByKey()

    println(category2SessionCountRDD.toDebugString)

    // 获取各个品类的session统计数量的前10
    val resultRDD: RDD[(Long, List[(String, Int)])] = category2SessionCountRDD.mapValues {
      iter => {
        iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
      }
    }

    resultRDD.collect().foreach(println)
    sc.stop()
  }

}
