package com.bigdata.spark.sql

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @author wuchonghua
 * @create 2021-10-26 11:36
 */
object TestDropView {
  def main(args: Array[String]): Unit = {
    val sparkSession  = SparkSession.builder().master("local[*]").appName("aa").config("spark.default.parallelism", "23").config("spark.sql.shuffle.partitions", "27").getOrCreate()

    val rdd = sparkSession.sparkContext.makeRDD(List(Row(1, 2), Row(3, 4), Row(3, 4), Row(3, 4), Row(3, 4), Row(3, 4), Row(3, 4), Row(3, 4)))
    val va = sparkSession.createDataFrame(rdd, StructType.apply(List(StructField("asda", IntegerType), StructField("b", IntegerType))))
    va.createOrReplaceTempView("V_A")  // 创建V_A
    val vb = sparkSession.sql("select * from V_A where asda = 3")
    vb.createOrReplaceTempView("V_B")  // 使用V_A创建V_B
    // sparkSession.catalog.dropTempView("V_A")  // 删除V_A
    sparkSession.sql("drop view V_A") // 删除V_A
    // sparkSession.sql("alter table V_B rename to V_C")
    println("--------------------------")
    sparkSession.sql("select * from V_B ").show(10)   // 使用V_B




    sparkSession.stop()
  }
}
