package com.xiaoke1256.orders.search;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
	public SqlSessionFactory sqlSessionFactory() {
		try {
			ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
			SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
			sqlSessionFactoryBean.setDataSource(dataSource());
			sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/sqlMapConfig.xml"));
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/xiaoke1256/orders/search/dao/*Mapper.xml"));
			return sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate() {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate((SqlSessionFactory) sqlSessionFactory());
		return sqlSessionTemplate;
	}
	
	//事务管理器
	@Bean
	public DataSourceTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}
}
