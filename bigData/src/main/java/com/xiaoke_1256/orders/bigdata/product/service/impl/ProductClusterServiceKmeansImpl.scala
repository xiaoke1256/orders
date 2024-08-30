package com.xiaoke_1256.orders.bigdata.product.service.impl

import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult
import com.xiaoke_1256.orders.bigdata.product.dto.{ProductWithStatic, SimpleProductStatic}
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.springframework.stereotype.Service

import scala.math.BigDecimal.javaBigDecimal2bigDecimal


/**
 * Kmeans 聚类算法,训练模型
 * 针对商品，两个维度：价格，年销量
 */
@Service
class ProductClusterServiceKmeansImpl {

  val appName = "kmeansCluster"
  // 设定spark master（默认支持local）
  val master = "local"

  /**
   * 聚类算法训练
   * @param productList 全部的商品
   * @param numClusters 最大分类数
   * @param numIterator 迭代次数
   * @param modelPath 模型输出到一个流中（可空）
   */
  def trainModel(productList:java.util.List[SimpleProductStatic],numClusters: Int,numIterator:Int,modelPath:String): Unit = {
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new JavaSparkContext(conf)

    //sc.parallelize(productList).foreach(p=>{println(p.getProductPrice());println(p.getOrderCount)});

    val parsedData = sc.parallelize(productList).map(p=>Vectors.dense(p.getProductPrice.toDouble/1000.0,p.getOrderCount.toDouble)).cache()
    val model = KMeans.train(parsedData,numClusters,numIterator);

    println("模型训练完成")
    model.clusterCenters.foreach(println)
    if(modelPath!=null && "".equals(modelPath.trim)){
      model.toPMML(modelPath);
    }
  }

  def modelDetail(modelPath:String): Object = {
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new JavaSparkContext(conf)
    val model = KMeansModel.load(sc,modelPath)
    model.clusterCenters
  }

  /**
   * 预测
   * @param modelPath
   * @param productList
   * @return
   */
  def predict(modelPath:String,productList:java.util.List[SimpleProductStatic]): java.util.List[PredictResult[SimpleProductStatic]] = {
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new JavaSparkContext(conf)

    val model = KMeansModel.load(sc,modelPath)

    sc.parallelize(productList).map((p)=>{
      val label = model.predict(Vectors.dense(p.getProductPrice.toDouble/1000.0,p.getOrderCount.toDouble))
      new PredictResult[SimpleProductStatic](p,label)
    }).collect()
  }
}
