package com.xiaoke_1256.orders.bigdata.common.ml

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.springframework.context.annotation.{Bean, Configuration}

@Configuration
class SparkConfig {


  //@Value("${spark.master}")
  private val sparkMaster: String = "spark://192.168.249.107:7077";

  //@Value("${spark.appName}")
  private val sparkAppName: String = "orders-bigdata-algorithm";

  //@Value("${spark.sql.shuffle.partitions}")
  private val sparkPartions: Long = 1;

  //@Value("${spark.yarn.maxAppAttempts}")
  private val sparkMaxAppAttempts: Long = 1;

  //@Value("${spark.driver.host}")
  private val sparkDriverHost: String = "192.168.40.1";

//  @Bean
//  def sparkSession(): SparkSession = {
//    val spark = SparkSession.builder()
//      .config("spark.sql.shuffle.partitions", 1)
//      .appName("orders-bigdata-algorithm")
//      .config("spark.yarn.maxAppAttempts", 1)
//      .master("local[*]")
//      //.enableHiveSupport()
//      .getOrCreate()
//
//    spark
//  }

  @Bean
  def sparkSession(): SparkSession = {
    //    val ipAddress: String = java.net.InetAddress.getLocalHost.getHostAddress
    //    val log = LogFactory.getLog(classOf[SparkConfig])
    //    log.info("ipAddress = " + ipAddress)
    // 创建一个SparkConf对象
    val conf = new SparkConf()
      .setAppName(sparkAppName)
      .setMaster(sparkMaster)
      .set("spark.sql.shuffle.partitions", sparkPartions.toString)
      .set("spark.yarn.maxAppAttempts", sparkMaxAppAttempts.toString)
      //      .set("spark.dynamicAllocation.enabled", sparkDynamicAllocationEnabled)
      //      .set("spark.executor.instances", sparkExecutorInstances.toString)
      .set("spark.blockManager.port", "10025")
      .set("spark.driver.blockManager.port", "10026")
      //.set("spark.driver.port", "10027") //make all communication ports static (not necessary if you disabled firewalls, or if your nodes located in local network, otherwise you must open this ports in firewall settings)
      //.set("spark.cores.max", "12")
      //.set("spark.executor.cores", "2")
      .set("spark.executor.memory", "512m")
      .set("spark.driver.host", sparkDriverHost)
      .set("spark.driver.bindAddress", "0.0.0.0")
      .setJars(List("D:\\maven-repository\\com\\xiaoke_1256\\orders\\bigData-spark\\1.0.0\\bigData-spark-1.0.0.jar"))

    val spark = SparkSession.builder()
      .config(conf)
      //      .enableHiveSupport()
      .getOrCreate()

    spark
  }

}
