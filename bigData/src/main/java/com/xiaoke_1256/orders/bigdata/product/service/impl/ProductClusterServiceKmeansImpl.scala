package com.xiaoke_1256.orders.bigdata.product.service.impl

import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.io.File
import scala.collection.JavaConverters


/**
 * Kmeans 聚类算法,训练模型
 * 针对商品，两个维度：价格，年销量
 */
@Service
class ProductClusterServiceKmeansImpl {

  @Autowired
  private val sparkSession:SparkSession = null ;

  /**
   * 聚类算法训练
   * @param productList 全部的商品
   * @param numClusters 最大分类数
   * @param numIterator 迭代次数
   * @param modelPath 模型输出到一个流中（可空）
   */
  def trainModel(productList:java.util.List[SimpleProductStatic],numClusters: Int,numIterator:Int,modelPath:String): Unit = {
    val sc = sparkSession.sparkContext

    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq//把List转成Seq
    val parsedData = sc.parallelize[SimpleProductStatic](productListSeq)
      .filter(p=>p.getOrderCount>0)
      .map(p=>Vectors.dense(p.getProductPrice/1000.0,p.getOrderCount.toDouble)).cache()
    val model = KMeans.train(parsedData,numClusters,numIterator);

    println("模型训练完成")
    model.clusterCenters.foreach(println)
    if(modelPath!=null && !"".equals(modelPath.trim)){
//      val file = new File(modelPath)
//      if(!file.exists()){
//        file.createNewFile()
//      }
      model.save(sc,modelPath)
    }
  }

  def modelDetail(modelPath:String): Object = {
    val sc = sparkSession.sparkContext
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
    val sc = sparkSession.sparkContext
    val model = KMeansModel.load(sc,modelPath)

    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq//把List转成Seq

    val result = sc.parallelize(productListSeq).map((p)=>{
      val label = model.predict(Vectors.dense(p.getProductPrice.toDouble/1000.0,p.getOrderCount.toDouble))
      new PredictResult[SimpleProductStatic](p,label)
    }).collect()
    JavaConverters.seqAsJavaList(result);
  }
}
