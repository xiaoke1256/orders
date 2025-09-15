package com.xiaoke1256.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关应用程序入口
 * 限流配置已在SentinelGatewayConfig和SentinelNacosConfig类中实现
 * Sentinel限流规则从Nacos动态加载，实现持久化管理
 */
@SpringBootApplication(
        scanBasePackages={"com.xiaoke1256.orders","org.springframework.http.codec"},
        exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
    }
}
