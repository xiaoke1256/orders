package com.xiaoke1256.orders;

import com.xiaoke1256.common.utils.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Import(RedisConfig.class)
public class SpringbootApplication {
	
	@Bean
	@LoadBalanced
	@Profile("prod")
	public RestTemplate balancedRestTemplate() {
		return new RestTemplate();
	}
	
	/**
	 * @deprecated 用feign 后此bean就失效了。
	 * @return
	 */
	@Deprecated
	@Bean
	@Profile({"dev","test"})
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }
}
