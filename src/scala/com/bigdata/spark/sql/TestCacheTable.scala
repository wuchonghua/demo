package com.bigdata.spark.sql

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StructField, StructType}

/**
 * @author wuchonghua
 * @create 2021-12-23 17:06
 */
object TestCacheTable {

  def main(args: Array[String]): Unit = {
    val sparkSession  = SparkSession.builder().master("local[*]").appName("aa").config("spark.default.parallelism", "1").config("spark.sql.shuffle.partitions", "27").getOrCreate()

    val rdd = sparkSession.sparkContext.makeRDD(List(Row(1, 2), Row(3, 4), Row(3, 5), Row(3, 6), Row(1, 7), Row(1, 8)))
    val value = rdd.map(
      r => {
        println(r + "@@@@@@")
        Row(r.getAs[Int](0) + 1, r.getAs[Int](1))
      }
    )

    val i = 2
    var va = sparkSession.createDataFrame(value, StructType.apply(List(StructField("c1", IntegerType), StructField("c2", IntegerType))))
    va = va.union(sparkSession.createDataFrame(sparkSession.sparkContext.makeRDD(List(Row(1, i.asInstanceOf[Any]))), StructType.apply(List(StructField("c1", IntegerType), StructField("c2", IntegerType)))))
    //va.cache()
    va.createOrReplaceTempView("V_A")  // 创建V_A
    sparkSession.sql("cache table v_a")

    val vb = sparkSession.sql("select * from V_A where c1 = 4")
    val vc = sparkSession.sql("select * from V_A where c1 = 2")


    vb.cache()
    vb.createOrReplaceTempView("V_B")  // 使用V_A创建V_B
//    sparkSession.sql("cache table v_b")

    vc.cache()
    vc.createOrReplaceTempView("V_C")  // 使用V_A创建V_C
//    sparkSession.sql("cache table v_c")


    //sparkSession.catalog.dropTempView("V_A")  // 删除V_A  使缓存失效
    //sparkSession.sql("drop view V_A") // 删除V_A 使缓存失效
    va.unpersist() // 使缓存失效

    // 不会影响缓存
    //sparkSession.sql("alter table V_A rename to V_D")

    println("--------------------------")
    sparkSession.sql("select * from V_B").show(10)   // 使用V_B
    sparkSession.sql("select * from V_C").show(10)   // 使用V_C

    // 测试执行计划的差别
//    sparkSession.sql("select * from V_C t1, V_B t2 where t1.c1 = t2.c2 and t1.c1 = 2").explain()
//    println("--------------------------")
//    sparkSession.sql("select * from V_C t1 join V_B t2 on t1.c1 = t2.c2 where t1.c1 = 2").explain()

    // 测试aggregate函数
//    println("--------------------------")
//    sparkSession.sql("select aggregate(array(1L, 2L, 3L), 0L, (acc, x) -> acc | x ) as xxx").printSchema()

    //val ve = sparkSession.sql("select * from V_D where c1 = 2 and c2 = 8")
    //ve.show(10)

    sparkSession.stop()
  }

}
