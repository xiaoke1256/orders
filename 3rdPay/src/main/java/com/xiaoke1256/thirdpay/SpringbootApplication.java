package com.xiaoke1256.thirdpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 本应用用来模拟第三方支付平台
 * @author Administrator
 *
 */
@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration
@EnableDiscoveryClient
@MapperScan("com.xiaoke1256.thirdpay.payplatform.dao")
public class SpringbootApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootApplication.class);
    }
	
	public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
