package com.xiaoke_1256.orders.bigdata.product

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.util.MLUtils


/**
 * Kmeans 聚类算法
 * 针对商品，两个维度：价格，年销量
 */
object KmeansCluster {
  val appName = "kmeansCluster"
  // 设定spark master（默认支持local）
  val master = "local"

  val filePath = "D:\\test\\input\\KmeansCluster.txt"; //每一行的格式为：

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new JavaSparkContext(conf)
    val data = MLUtils.loadLibSVMFile(sc,filePath)
    val parsedData = data.map( s => s.getFeatures).cache()
    //
    val numClusters = 5;//最大分类数
    val numIterator = 100;//迭代次数
    val model = KMeans.train(parsedData,numClusters,numIterator);
    model.clusterCenters.foreach(println)
  }
}
