package com.xiaoke1256.orders.gateway.config;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 提供RestTemplate的Bean用于HTTP请求，并集成Sentinel监控
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建集成Sentinel的RestTemplate实例
     * 通过SentinelRestTemplate注解，使RestTemplate支持Sentinel限流、熔断等功能
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}