package com.bigdata.spark.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col

/**
 * @author wuchonghua
 * @create 2024-03-21 15:34
 */
object TestPartitionTableCache {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").appName("test1")
      .config("spark.sql.shuffle.partitions", "2")
      .config("spark.sql.autoBroadcastJoinThreshold", "-1")
      .config("spark.local.dir", "/tmp/spark")
      .getOrCreate()

    spark.sparkContext.setCheckpointDir("datas/datas1")

    val frame1 = spark.read.option("header", "true").csv("datas/hadlink.csv").repartition(col("link_pid")).cache()
    frame1.createOrReplaceTempView("had_link")
    val value1 = frame1.checkpoint()
    frame1.foreach(r => println(r))
    value1.createOrReplaceTempView("had_link")


    val frame2 = spark.read.option("header", "true").csv("datas/hadlinkdirect.csv").repartition(col("link_lgpa_pid")).cache()
    frame2.createOrReplaceTempView("had_link_direct")
    val value2 = frame2.checkpoint()
    frame2.foreach(r => println(r))
    value2.createOrReplaceTempView("had_link_direct")

    val frame3 = spark.read.option("header", "true").csv("datas/hadlinklg.csv").repartition(col("link_pid")).cache()
    frame3.createOrReplaceTempView("had_link_lg")
    val value3 = frame3.checkpoint()
    frame3.foreach(r => println(r))
    value3.createOrReplaceTempView("had_link_lg")


//    val frame1 = spark.read.option("header", "true").csv("datas/hadlink.csv").cache()
//    frame1.createOrReplaceTempView("had_link")
//    frame1.foreach(r => println(r))
//
//    val frame2 = spark.read.option("header", "true").csv("datas/hadlinkdirect.csv").cache()
//    frame2.createOrReplaceTempView("had_link_direct")
//    frame2.foreach(r => println(r))
//
//    val frame3 = spark.read.option("header", "true").csv("datas/hadlinklg.csv").cache()
//    frame3.createOrReplaceTempView("had_link_lg")
//    frame3.foreach(r => println(r))

    spark.sql("select distinct link_pid from had_link").foreach(r => println(r))
    spark.sql("select link_lgpa_pid, min(direct) from had_link_direct group by link_lgpa_pid").foreach(r => println(r))
    spark.sql("select * from had_link t1 join had_link_lg t2 on t1.link_pid = t2.link_pid").foreach(r => println(r))
    spark.sql("select link_pid, sum(length) from had_link group by link_pid").foreach(r => println(r))
    spark.sql("select distinct link_lgpa_pid from had_link_direct").foreach(r => println(r))
    spark.sql("select * from had_link t1 join had_link_lg t2 on t1.link_pid = t2.link_pid join had_link_direct t3 on t2.link_lgpa_pid = t3.link_lgpa_pid").foreach(r => println(r))

    println("-----------------")
    Thread.sleep(3600 * 1000)
    spark.stop()


  }


}
