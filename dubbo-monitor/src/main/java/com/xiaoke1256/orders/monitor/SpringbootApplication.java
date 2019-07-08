package com.xiaoke1256.orders.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication
@ImportResource(value = "classpath:dubbo-config.xml")
@DubboComponentScan(basePackages = "com.xiaoke1256.orders")
public class SpringbootApplication extends SpringBootServletInitializer {
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootApplication.class);
    }
	
	public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
