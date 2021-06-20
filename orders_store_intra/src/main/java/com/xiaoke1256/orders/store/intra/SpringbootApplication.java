package com.xiaoke1256.orders.store.intra;

//import org.mybatis.spring.annotation.MapperScan;
import com.xiaoke1256.orders.store.intra.encrypt.HMAC256;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages="com.xiaoke1256.orders.store.intra",exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class SpringbootApplication extends SpringBootServletInitializer {

//    @Value("login.token.secret")
//    private String loginSecret;

//    @Value("login.token.secret")
//    private Long loginExpire;

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringbootApplication.class);
    }
	
	public static void main(String[] args) {
	    try {
            SpringApplication.run(SpringbootApplication.class, args);
        }catch(Throwable t){
	        t.printStackTrace();
        }
    }

    /*
    @Bean
    public HMAC256 loginTokenGenerator(){
	    return new HMAC256(loginExpire,loginSecret);
    }
    */

}
