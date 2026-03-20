package com.bigdata.spark.sql;

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

/**
 * @author wuchonghua
 * @create 2023-02-01 14:08
 */
object TestCheckPoint {

  def main(args: Array[String]): Unit = {
    val sparkSession  = SparkSession.builder().master("local[*]").appName("aa").getOrCreate()
    sparkSession.sparkContext.setCheckpointDir("datas/datas1")
    val rdd = sparkSession.sparkContext.makeRDD(List(Row(1, 2), Row(3, 4)))
    val df = sparkSession.createDataFrame(rdd, StructType.apply(List(StructField("a", IntegerType), StructField("b", IntegerType))))
    df.createOrReplaceTempView("V_A")
    sparkSession.sql("cache table V_A")
    val checkPointDf = df.checkpoint(false)
    checkPointDf.createOrReplaceTempView("V_A")
    sparkSession.sql("select * from V_A").show(20)

    sparkSession.stop()
  }
}

