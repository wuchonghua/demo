package com.bigdata.spark.core.practice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author wuchonghua
 * @create 2021-10-03 12:43
 */
object JumpStatistics{

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("app").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)
    val lineRDD: RDD[String] = sc.textFile("datas/user_visit_action.txt")
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
    userVisitActionRDD.cache()

    val statisticsPages = List(1,2,3,4,5,6,7)
    val statisticsJumpPages = statisticsPages.zip(statisticsPages.tail)

    val page2CountRDD = userVisitActionRDD.filter(
      // 取出List中除最后一个元素之外的其他元素，因为最后一个元素不会成为分母
      userVisitAction => statisticsPages.init.contains(userVisitAction.page_id)
    ).map(
      userVisitAction => {
        (userVisitAction.page_id, 1)
      }
    ).reduceByKey(_ + _)
    val page2CountMap: Map[Long, Int] = page2CountRDD.collect().toMap

    // 不同session下的记录，之间没有关系，不能转换成页面跳转数据
    // 同一session下的记录分组在一起
    val sessionId2VisitActionRDD: RDD[(String, Iterable[UserVisitAction])] = userVisitActionRDD.groupBy(_.session_id)
    // 同一session下的记录转换成跳转数据
    val jumpPage2CountRDD: RDD[((Long, Long), Int)] = sessionId2VisitActionRDD.flatMap {
      case (_, userActions) => {
        // 同一session的访问page根据时间排序
        // 因为分布式服务的日志打印，同一session访问到不同的服务可能日志记录会出现错行
        val orderedPageIds: List[Long] = userActions.toList.sortBy(_.date).map(_.page_id)
        // [1,2,3,4,5,6]
        // [2,3,4,5,6]  取出list中除第一个元素之外的其他元素
        // zip 拉链 将List中前一个和后一个拉在一起 就是从第一个页面跳转到第二个页面
        val jumpPages: List[(Long, Long)] = orderedPageIds.zip(orderedPageIds.tail)
        val jumpPageCount1List: List[((Long, Long), Int)] = jumpPages.filter(
          // 需要拉链在一起后 再过滤
          // 因为在userVisitActionRDD或分组后的userActions中直接过滤的话，会得到错误的jumpPage
          // 比如1-8-2-3-9-4 直接过滤得到1-2-3-4 其实只有2-3符合
          jumpPage => statisticsJumpPages.contains(jumpPage)
        ).map {
//          case (page1, page2) => {
//            ((page1, page2), 1)
//          }
          (_, 1)
        }
        jumpPageCount1List
      }
    }.reduceByKey(_ + _)

    val resultRDD: RDD[((Long, Long), Double)] = jumpPage2CountRDD.map {
      case ((page1, page2), jumpPageCount) => {
        val page1Count = page2CountMap(page1)
        ((page1, page2), jumpPageCount.toDouble / page1Count)
      }
    }

    resultRDD.collect().foreach(println)

    sc.stop()
  }

}
//用户访问动作表
case class UserVisitAction(
                            date: String,//用户点击行为的日期
                            user_id: Long,//用户的 ID
                            session_id: String,//Session 的 ID
                            page_id: Long,//某个页面的 ID
                            action_time: String,//动作的时间点
                            search_keyword: String,//用户搜索的关键词
                            click_category_id: Long,//某一个商品品类的 ID
                            click_product_id: Long,//某一个商品的 ID
                            order_category_ids: String,//一次订单中所有品类的 ID 集合
                            order_product_ids: String,//一次订单中所有商品的 ID 集合
                            pay_category_ids: String,//一次支付中所有品类的 ID 集合
                            pay_product_ids: String,//一次支付中所有商品的 ID 集合
                            city_id: Long //城市 id
                          )