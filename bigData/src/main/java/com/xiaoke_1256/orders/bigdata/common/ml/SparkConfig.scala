package com.xiaoke_1256.orders.bigdata.common.ml

import org.apache.spark.sql.SparkSession
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SparkConfig {

  @Bean
  def sparkSession(): SparkSession = {
    val spark = SparkSession.builder()
      .config("spark.sql.shuffle.partitions", 1)
      .appName("orders-bigdata-algorithm")
      .config("spark.yarn.maxAppAttempts", 1)
      .master("local[*]")
      //.enableHiveSupport()
      .getOrCreate()

    spark
  }

}
