package com.xiaoke_1256.orders

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaPairRDD
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.storage.StorageLevel
import scala.Tuple2
import java.util

class SparkMain{
  @throws[Exception]
  def main(args: Array[String]): Unit = {

    val appName = "testSpark"
    // 设定spark master（默认支持local）
    val master = "local"
    // 处理的源文件，输出的结果，这个文件是咱们前几天在MapReduce中的文件
    val filePath = "/test/input/testFile.txt"
    val outputPath = "/test/output/testSpartResult"
    // 初始化Spark环境，为后边运行读取环境配置
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    val sc = new JavaSparkContext(conf)
    // 读取文件并处理
    val lines = sc.textFile(filePath)

    // 将每一行通过空格截取成新的RDD
    //val words = lines.flatMap[Array[String]]((s: String) => util.Arrays.asList(s.split(" "):_*))
    val words = lines.flatMap((s: String) => util.Arrays.asList(s.split(" "):_*).iterator)

    // 将所有的文字组成键值对，并对不同的key进行计数
    val pairs = words.mapToPair((s:String) => new Tuple2[String,Integer](s, 1))
    val counts = pairs.reduceByKey((a: Integer, b: Integer) => a + b)
    // 循环输出每一个字的出现次数
    counts.foreach((s: Tuple2[String, Integer]) => System.out.println(s._1 + "," + s._2))
    // 持久化到内存和硬盘中，能够为后期新的程序方便读取
    counts.persist(StorageLevel.MEMORY_AND_DISK)
    // 输出成文本到指定目录
    counts.saveAsTextFile(outputPath)
  }
}