package com.xiaoke_1256.orders.bigdata.product.service.spark

import com.typesafe.scalalogging.slf4j.Logger
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import java.io.{File, PrintWriter}


/**
 * Kmeans 聚类算法,训练模型
 * 针对商品，两个维度：价格，年销量
 */
@Service
class ProductClusterServiceKmeansImpl {

  @Autowired
  private val sparkSession:SparkSession = null ;

  private val logger = Logger(LoggerFactory.getLogger(this.getClass));

  /**
   * 聚类算法训练
   * @param samplePath 训练样例数据
   * @param numClusters 最大分类数
   * @param numIterator 迭代次数
   * @param modelPath 模型输出到一个流中（可空）
   */
  def trainModel(samplePath:String,numClusters: Int,numIterator:Int,modelPath:String
                 ,productPriceCoefficient:Double,orderCountCoefficient:Double): Unit = {
    println("modelPath:"+modelPath)
    val sc = sparkSession.sparkContext

    //HDFS上的目标路径
    //val hdfsPath = "hdfs://192.168.249.107:8020/your/hdfs/path/file.txt"

    //将文件上传到HDFS
    //sc.addFile(samplePath)

//    val data = MLUtils.loadLibSVMFile(sc,samplePath)
//    val parsedData = data.map( s => s.getFeatures).map((f)=>Vectors.dense(f(0)*productPriceCoefficient,f(1)*orderCountCoefficient)).cache()
    logger.info("开始加载数据")
    val libSVMData = sparkSession.read.format("libsvm").load(samplePath)
    logger.info("转换数据")
    val parsedData = libSVMData.select("features").rdd.map(row => row.getAs[org.apache.spark.ml.linalg.Vector](0))
      .filter( (f)=>f(1)>0)
      .map((f)=>Vectors.dense(f(0)/1000.0*productPriceCoefficient,f(1)*orderCountCoefficient)).cache()
    //val labels = libSVMData.select("label").rdd.map(row => row.getDouble(0))
    logger.info("开始训练模型")
    val model = KMeans.train(parsedData,numClusters,numIterator);

    logger.info("模型训练完成")
    model.clusterCenters.foreach(println)
    logger.info("模型训练完成")
    if(modelPath!=null && !"".equals(modelPath.trim)){
      //      val file = new File(modelPath)
      //      if(!file.exists()){
      //        file.createNewFile()
      //      }
      model.save(sc,modelPath)
      logger.info("模型保存完成")
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
  def predict(modelPath:String,simplePath:String,resultPath:String,productPriceCoefficient:Double,orderCountCoefficient:Double) = {
    val sc = sparkSession.sparkContext
    val model = KMeansModel.load(sc,modelPath)

    val data = MLUtils.loadLibSVMFile(sc,simplePath)

    val predictedData = data.filter( (d)=>d.getFeatures(1).toDouble>0).map((d)=>{
      val label = model.predict(Vectors.dense(d.getFeatures(0).toDouble/1000.0*productPriceCoefficient,d.getFeatures(1).toDouble*orderCountCoefficient))
      // index price count label
      d.label+" "+d.getFeatures(0)+" "+d.getFeatures(1)+" "+label
    })
    println("resultPath:"+resultPath)
    predictedData.coalesce(1,true).saveAsTextFile(resultPath);
  }
}
