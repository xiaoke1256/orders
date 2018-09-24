package com.xiaoke1256.orders.search;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SpringbootApplication {
	@Bean
	public PropertiesFactoryBean prop() {
		PropertiesFactoryBean prop = new PropertiesFactoryBean();
		Properties properties = new Properties();
		properties.put("locations", new String[]{"classpath:public.properties"});
		prop.setProperties(properties );
		return prop;
	}

}
