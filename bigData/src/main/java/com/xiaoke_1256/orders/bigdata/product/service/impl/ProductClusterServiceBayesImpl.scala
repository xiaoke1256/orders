package com.xiaoke_1256.orders.bigdata.product.service.impl

import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult
import com.xiaoke_1256.orders.bigdata.product.dto.{ProductWithLabel, SimpleProductStatic}
import com.xiaoke_1256.orders.bigdata.product.model.Product
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.JavaConverters

/**
 * 针对商品,有以下维度判断：品牌、商铺(、种类)
 */
class ProductClusterServiceBayesImpl {

  @Autowired
  private val sparkSession:SparkSession = null ;

  def train(args:Array[String],productList:java.util.List[ProductWithLabel],modelPath:String): Unit = {
    val sc = sparkSession.sparkContext;
    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq
    val parsedData = sc.parallelize[ProductWithLabel](productListSeq)
      .map(p=>new LabeledPoint(p.getLabel.toDouble,Vectors.dense(p.getBrand.hashCode,p.getStoreNo.hashCode))  ).cache() //
    val model = NaiveBayes.train(parsedData);
    println("模型训练完成")
    model.labels.foreach(println);
    model.pi.foreach(println);
    model.save(sc,modelPath)
  }

  /**
   * 预测
   * @param modelPath
   * @param productList
   * @return
   */
  def predict(modelPath:String,productList:java.util.List[Product]
              ,productPriceCoefficient:Double,orderCountCoefficient:Double): java.util.List[PredictResult[Product]] = {
    val sc = sparkSession.sparkContext
    val model = NaiveBayesModel.load(sc,modelPath)

    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq//把List转成Seq

    val result = sc.parallelize(productListSeq).map((p)=>{
      val label = model.predict(Vectors.dense(p.getBrand.hashCode,p.getStoreNo.hashCode))
      new PredictResult[Product](p,label.intValue())
    }).collect()
    JavaConverters.seqAsJavaList(result);
  }
}
