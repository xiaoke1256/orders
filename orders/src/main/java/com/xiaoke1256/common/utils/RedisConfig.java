package com.xiaoke1256.common.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisUtils redisUtils() {
		return new RedisUtils();
	}
}
