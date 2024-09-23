package com.xiaoke_1256.orders.bigdata.product.service.spark

import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.SparkSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import scala.collection.JavaConverters

/**
 * 针对商品,有以下维度判断：品牌、商铺(、种类)
 */
@Service
class ProductClusterServiceBayesImpl {

  @Autowired
  private val sparkSession:SparkSession = null ;

  def train(productList:java.util.List[SimpleProductStatic],modelPath:String): Unit = {
    val sc = sparkSession.sparkContext;
    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq
    //sc.parallelize[SimpleProductStatic](productListSeq).filter((p)=>p.getLabel!=null).foreach((p)=>println(p.getStoreNo))
    val parsedData = sc.parallelize[SimpleProductStatic](productListSeq).filter((p)=>p.getLabel!=null)
      .map(p=>new LabeledPoint(p.getLabel.toDouble,Vectors.dense((if(p.getBrand==null) 0 else p.getBrand.hashCode), p.getStoreNo.toInt))  ).cache() //
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
  def predict(modelPath:String,productList:java.util.List[SimpleProductStatic]): java.util.List[PredictResult[SimpleProductStatic]] = {
    val sc = sparkSession.sparkContext
    val model = NaiveBayesModel.load(sc,modelPath)

    val productListSeq = JavaConverters.asScalaIteratorConverter(productList.iterator()).asScala.toSeq//把List转成Seq

    val result = sc.parallelize(productListSeq).map((p)=>{
      val label = model.predict(Vectors.dense((if(p.getBrand==null) 0 else p.getBrand.hashCode),p.getStoreNo.toInt))
      new PredictResult[SimpleProductStatic](p,label.intValue())
    }).collect()
    JavaConverters.seqAsJavaList(result);
  }
}
