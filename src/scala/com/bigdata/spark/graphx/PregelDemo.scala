package com.bigdata.spark.graphx

import org.apache.spark.SparkContext
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @author wuchonghua
 * @create 2022-03-10 20:09
 */
object PregelDemo extends Serializable {
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
    val rdd = sc.makeRDD(List((9L, "Ivy")))
//    val init = graph.mapVertices {
//      case (id, people) =>
//        if (people.name == "Alice") {
//          (people, 1)
//        } else {
//          (people, -1)
//        }
//    }
//    val mincc = init.pregel(-1, 20, EdgeDirection.Out)(
//      (id, attr, msg) => if (attr._2 == 1) attr else (attr._1, msg),
//      (triplet) => {
//        if (triplet.srcAttr._2 == 1 && triplet.dstAttr._2 == -1) {
//          Iterator((triplet.dstId, 1))
//        } else {
//          Iterator.empty
//        }
//      },
//      (a, b) => a
//    )

    val init = graph.outerJoinVertices(rdd) {
      case (id, people, u) =>
        if (u.isDefined) {
          (people, 1)
        } else {
          (people, -1)
        }
    }
    val mincc = init.pregel(-1, 20, EdgeDirection.In)(
      (id, attr, msg) => if (attr._2 == 1) attr else (attr._1, msg),
      (triplet) => {
        if (triplet.dstAttr._2 == 1 && triplet.srcAttr._2 == -1) {
          Iterator((triplet.srcId, 1))
        } else {
          Iterator.empty
        }
      },
      (a, b) => a
    )

    mincc.vertices.collect()foreach(println)
    mincc.subgraph(vpred = {
      case (x, y) => y._2 != -1
    }).vertices.collect().foreach(println)
  }
}