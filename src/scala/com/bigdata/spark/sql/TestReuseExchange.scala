package com.bigdata.spark.sql

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, count, countDistinct, lit}

/**
 * @author wuchonghua
 * @create 2025-02-25 13:54
 */
object TestReuseExchange {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val df = spark.read.option("header", "true").csv("datas/hadlinklg.csv").repartition(col("link_pid")).cache()

    val df1 = df.groupBy("link_pid").agg(count("link_lgpa_pid").alias("value")).collect()
    val df2 = df.groupBy("link_pid").agg(countDistinct("link_lgpa_pid").alias("value"))


    df2.explain(true)

  }

}
