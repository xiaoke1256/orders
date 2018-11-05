package com.xiaoke1256.orders.search;

import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan(basePackages= {"com.xiaoke1256.orders.search"})
@EnableWebMvc
public class SpringApplication extends WebMvcConfigurerAdapter  {
	/**
	 * properties
	 * @return
	 */
	@Bean("props")
	public FactoryBean<Properties> prop() {
		PropertiesFactoryBean prop = new PropertiesFactoryBean();
		Properties properties = new Properties();
		properties.put("locations", new String[]{"classpath*:config/elasticsearch-config.properties",
				"classpath*:config/db.properties"});
		prop.setProperties(properties );
		return prop;
	}
	
	/**
	 * spring mvc配置
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver(){
		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	
}
