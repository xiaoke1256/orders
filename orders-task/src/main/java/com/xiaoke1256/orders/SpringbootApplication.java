package com.xiaoke1256.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

//@EnableCircuitBreaker
//@EnableFeignClients
@SpringBootApplication
//@EnableDiscoveryClient
public class SpringbootApplication extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootApplication.class);
    }
	
	@Bean
	//@LoadBalanced
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
