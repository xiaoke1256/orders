package com.xiaoke_1256.orders.bigdata.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 关于hdfs的配置
 */
@Configuration
public class HdfsConfig {

    @Value("${spark.hdfs.uri}")
    private String hdfsUri;

    @Bean
    public org.apache.hadoop.conf.Configuration hdfsConfiguration(){
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.defaultFS", hdfsUri);
        return conf;
    }

}
