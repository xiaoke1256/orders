package com.xiaoke1256.orders.search;

import java.io.IOException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class DatabaseConfig {
	
	//@Value("${db.jndi_name}")
	private String jndiName="java:/comp/env/jdbc/product";
	//数据源
	@Bean
	public DataSource dataSource(){
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup(jndiName);
			return ds;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	//以下 Mybatis 配置 */
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() {
		try {
			ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
			SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
			sqlSessionFactory.setDataSource(dataSource());
			sqlSessionFactory.setConfigLocation(new ClassPathResource("config/sqlMapConfig.xml"));
			sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:com/xiaoke1256/orders/search/dao/*Mapper.xml"));
			return sqlSessionFactory;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
