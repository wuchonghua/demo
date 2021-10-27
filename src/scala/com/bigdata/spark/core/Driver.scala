package com.bigdata.spark.core

import java.io.ObjectOutputStream
import java.net.Socket

/**
 * @author wuchonghua
 * @create 2021-09-09 11:02
 */
object Driver {

  def main(args: Array[String]): Unit = {
    val client = new Socket("localhost", 8888)
    val out = client.getOutputStream
//    logic: Int => (Int, Int) = (x : Int) => {
//      (x, x + 10)
//    }
    val myRdd = new MyRDD()
    myRdd.data = List(1,2,3,4)
    myRdd.logic = (x : Int) => {
      (x, x + 10)
    }
    val oout = new ObjectOutputStream(out)
    oout.writeObject(myRdd)
    oout.flush()

    oout.close()
    out.close()
    client.close()

  }
}
