package com.bigdata.spark.sql

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

/**
 * @author wuchonghua
 * @create 2021-10-26 11:36
 */
object PartitionNum {
  def main(args: Array[String]): Unit = {
    val sparkSession  = SparkSession.builder().master("local[*]").appName("aa").config("spark.default.parallelism", "23").config("spark.sql.shuffle.partitions", "27").getOrCreate()

    val rdd = sparkSession.sparkContext.makeRDD(List(Row(1, 2), Row(3, 4)))
    val df = sparkSession.createDataFrame(rdd, StructType.apply(List(StructField("a", IntegerType), StructField("b", IntegerType))))
    df.createOrReplaceTempView("V_A")
    val df2 = sparkSession.sql("select * from V_A")
    val df3 = df.groupBy("a").count()
    println(df.rdd.getNumPartitions + "----------------------------------@@@@@@@@@@@@@@@@@@@@@@@") //23 spark.default.parallelism
    println(df2.rdd.getNumPartitions + "----------------------------------@@@@@@@@@@@@@@@@@@@@@@@") // 23  从父RDD继承
    println(df.rdd.groupBy(_.get(1)).getNumPartitions + "----------------------------------@@@@@@@@@@@@@@@@@@@@@@@") //23 RDD shuffle
    println(df3.rdd.getNumPartitions + "----------------------------------@@@@@@@@@@@@@@@@@@@@@@@") // 27  sql shuffle

    sparkSession.stop()
  }
}
