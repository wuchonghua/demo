package com.bigdata.spark.sql

import org.apache.spark.sql.types.{IntegerType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @author wuchonghua
 * @create 2021-10-26 11:36
 */
object TestCTE {
  def main(args: Array[String]): Unit = {
    val sparkSession  = SparkSession.builder().master("local[*]").appName("aa").config("spark.default.parallelism", "23").config("spark.sql.shuffle.partitions", "27").getOrCreate()

    val rdd = sparkSession.sparkContext.makeRDD(List(Row(1, 2), Row(2, 3), Row(3, 4)))
    val va = sparkSession.createDataFrame(rdd, StructType.apply(List(StructField("asda", IntegerType), StructField("b", IntegerType))))
    va.createOrReplaceTempView("V_A")  // 创建V_A
    val sql =
      """
        |with tmp1 as (select * from v_a where asda = 1),
        |tmp2 as (
        |select asda, b from tmp1 union all select asda, b from tmp2 t1 join V_A t2 on t1.b = t2.asda)
        |select * from tmp2
        |""".stripMargin
    sparkSession.sql(sql).show(3)






    sparkSession.stop()
  }
}
