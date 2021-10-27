package com.bigdata.spark.core

/**
 * @author wuchonghua
 * @create 2021-09-09 11:04
 */
class MyRDD extends Serializable {
  var data : List[Int] = _
  var logic: Int => (Int, Int) = _
  def map = {
    data.map(logic)
  }
}
