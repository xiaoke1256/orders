package com.xiaoke_1256.orders.bigdata.product

import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 协同过滤算法ALS算法
 */
object ALSCollaborativeFiltering {
  val appName = "ALSCollaborativeFiltering"
  // 设定spark master（默认支持local）
  val master = "local"

  val filePath = "D:\\test\\input\\ALS.txt"; //每一行的格式为：

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster(master).setAppName(appName);
    val sc = new SparkContext(conf)
    val data = sc.textFile(filePath)
    val ratings = data.map(_.split(' ')  match {
      case Array(user,item,rate) =>
        Rating(user.toInt,item.toInt,rate.toDouble);
    })
    val rank = 2;//设置隐藏因子
    val numIterations = 2;//迭代次数
    val lambda = 0.01//正则化参数
    val model = ALS.train(ratings,rank,numIterations,lambda);

    var rs = model.recommendProducts(1,2);
    rs.foreach(println)

  }
}
