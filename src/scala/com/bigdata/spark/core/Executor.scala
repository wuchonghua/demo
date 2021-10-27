package com.bigdata.spark.core

import java.io.ObjectInputStream
import java.net.ServerSocket

/**
 * @author wuchonghua
 * @create 2021-09-09 11:15
 */
object Executor {

  def main(args: Array[String]): Unit = {
    val serverSocket = new ServerSocket(8888)
    val client = serverSocket.accept()
    val in = client.getInputStream
    val oin = new ObjectInputStream(in)
    val myRdd: MyRDD = oin.readObject().asInstanceOf[MyRDD]
    println(myRdd.map)
    oin.close()
    in.close()
    client.close()
    serverSocket.close()
  }

}
