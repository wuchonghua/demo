package com.bigdata.spark.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
/**
 * @author wuchonghua
 * @create 2022-03-10 20:09
 */
object ConnectComponentDemo extends Serializable {
  case class Person(name:String,age:Int)
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("pregel")
      .master("local[*]").getOrCreate()
    val sc: SparkContext = spark.sparkContext

    case class People(name:String,age:Int)
    val peopleRDD: RDD[(VertexId, People)] = sc.textFile("C:\\Users\\Administrator\\Desktop\\people.csv")
      .map(_.split(","))
      .map(x => (x(0).toLong, People(x(1), x(2).toInt)))

    val linksRDD: RDD[Edge[String]] = sc.textFile("C:\\Users\\Administrator\\Desktop\\link.csv")
      .map { x =>
        val row = x.split(",")
        Edge(row(0).toInt, row(1).toInt, row(2))
      }
    val graph=Graph(peopleRDD,linksRDD)
    val mincc: Graph[VertexId, String] = graph.connectedComponents()
    mincc.vertices.collect()foreach(println)

    val newGraph: Graph[(VertexId, String, PartitionID), String]
    = mincc.outerJoinVertices(peopleRDD)((id,mincc,p)=>(mincc,p.get.name,p.get.age))

    mincc.vertices.map(_._2).collect.distinct.foreach (x =>{
      val sub: Graph[(VertexId, String, PartitionID), String] = newGraph.subgraph(vpred = (id1,id2) => id2._1==x)
      println(sub.triplets.collect().mkString(","))
    })
  }
}