package com.xiaoke1256.orders.search;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.TestPropertySource;

@Configuration
@EnableAsync
@PropertySource("classpath:config/db.properties")
@TestPropertySource("classpath:config/db.properties")
public class DatabaseConfig {
	
	@Value("${db.jndi_name}")
	private String jndiName;
	
	@Value("${db.connection.driver_class}")
	private String driverClassName;

	@Value("${db.connection.url}")
	private String url;

	@Value("${db.connection.username}")
	private String username;

	@Value("${db.connection.password}")
	private String password;
	
	//数据源
	@Bean
	@Profile({"dev","prod"})
	public DataSource dataSource(){ 
		try {
			Context context = new InitialContext();
			DataSource ds = (DataSource)context.lookup(jndiName);
			return ds;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Bean
	@Profile("test")
	public DataSource testDataSource(){
		DriverManagerDataSource ds =new DriverManagerDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}
	
	//以下 Mybatis 配置 */
	@Bean(name="sqlSessionFactoryBean")
	public SqlSessionFactory sqlSessionFactoryBean(DataSource testDataSource) {
		try {
			ResourcePatternResolver resolver = (ResourcePatternResolver) new PathMatchingResourcePatternResolver();
			SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
			sqlSessionFactoryBean.setDataSource(testDataSource);
			sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/sqlMapConfig.xml"));
			sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:com/xiaoke1256/orders/search/dao/*Mapper.xml"));
			return sqlSessionFactoryBean.getObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		return sqlSessionTemplate;
	}
	
	//事务管理器
	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager; 
	}
	
//	/**
//	 * 自动扫描配置
//	 * @return
//	 */
//	@Bean 
//	public MapperScannerConfigurer mapperScannerConfigurer() {
//		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//		mapperScannerConfigurer.setBasePackage("com.xiaoke1256.orders.search.dao");
//		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
//		mapperScannerConfigurer.setAnnotationClass(Repository.class);
//		return mapperScannerConfigurer;
//	}
}
