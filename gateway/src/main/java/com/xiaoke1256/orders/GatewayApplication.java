package com.xiaoke1256.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 网关应用程序入口
 * 全局限流配置已在 SentinelGatewayConfig 类中实现
 */
@SpringBootApplication(
        scanBasePackages={"com.xiaoke1256.orders","org.springframework.http.codec"},
        exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
    }
}
