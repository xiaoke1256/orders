package com.xiaoke1256.thirdpay.infrastructure.config;

import com.xiaoke1256.thirdpay.domain.event.DomainEventPublisher;
import com.xiaoke1256.thirdpay.infrastructure.event.SpringDomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 第三方支付模块配置类
 * 用于注册DDD相关组件和配置
 */
@Configuration
@ComponentScan({
    "com.xiaoke1256.thirdpay.domain",
    "com.xiaoke1256.thirdpay.application",
    "com.xiaoke1256.thirdpay.infrastructure",
    "com.xiaoke1256.thirdpay.interface"
})
public class ThirdPayConfig {
    
    /**
     * 配置领域事件发布器
     * @return 领域事件发布器实现
     */
    @Bean
    public DomainEventPublisher domainEventPublisher() {
        return new SpringDomainEventPublisher();
    }
    
    /**
     * 配置异步事件多播器
     * 确保事件监听器异步执行，避免影响主流程
     * @return 异步事件多播器
     */
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return multicaster;
    }
    
    /**
     * 配置支付相关属性
     * 后续可扩展为从配置文件加载
     */
    @Bean
    public PaymentProperties paymentProperties() {
        return new PaymentProperties();
    }
}