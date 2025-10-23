package com.xiaoke1256.orders.store.intra;

//import org.mybatis.spring.annotation.MapperScan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages={"com.xiaoke1256.orders.store.intra","com.xiaoke1256.orders.auth"},
        exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class OrdersStoreIntraApplication  {
	
	public static void main(String[] args) {
	    try {
            SpringApplication.run(OrdersStoreIntraApplication.class, args);
        }catch(Throwable t){
	        t.printStackTrace();
        }
    }

}
