package com.xiaoke1256.thirdpay;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 本应用用来模拟第三方支付平台
 * @author Administrator
 *
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude={HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration
@MapperScan("com.xiaoke1256.thirdpay.payplatform.dao")
public class ThirdPayApplication {

	public static void main(String[] args) {
        SpringApplication.run(ThirdPayApplication.class, args);
    }
}
