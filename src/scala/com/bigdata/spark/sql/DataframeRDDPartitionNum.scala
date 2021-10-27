package com.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DataType, StructField, StructType}
import org.apache.spark.sql.types._

import java.util
/**
 * @author wuchonghua
 * @create 2021-10-25 18:28
 */
object DataframeRDDPartitionNum {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("a").setMaster("local[*]").set("spark.default.parallelism", "21")
    val sparkSession = SparkSession.builder.config(conf).getOrCreate()
    val struct = StructType.apply(Seq(StructField("a", StringType) ))
    val rdd = sparkSession.sparkContext.makeRDD(Seq(Row("1"),Row("2"),Row("3")), 10)
    val l = new util.ArrayList[Row]()
    l.add(Row("1"))
    l.add(Row("2"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    l.add(Row("3"))
    val dataframe = sparkSession.createDataFrame(l, struct)//.repartition(30)
    val value = dataframe.rdd.map(r => r.getAs[String](0))
    println("---------------------" + value.getNumPartitions) // 从Seq创建RDD
    sparkSession.stop()
  }

}
