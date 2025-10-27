package com.xiaoke1256.orders.auth.components;

import com.xiaoke1256.orders.auth.encrypt.HMAC256;
import com.xiaoke1256.orders.common.util.ExpressionParserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.expression.ExpressionParser;

@Configuration
public class BeanConfig {
    
    @Value("${login.token.secret}")
    private String loginSecret;

    @Value("${login.session.expired}")
    private String loginExpireExp;

    @Value("${login.token.refresh_secret}")
    private String refreshSecret;

    @Value("${login.session.refresh_expired}")
    private String refreshExpireExp;

    @Bean
    public HMAC256 loginTokenGenerator(){
        return new HMAC256(ExpressionParserUtils.parse(loginExpireExp),loginSecret);
    }

    @Bean
    public HMAC256 refreshTokenGenerator(){
        //目前只支持计算乘法
        return new HMAC256(ExpressionParserUtils.parse(refreshExpireExp),refreshSecret);
    }
}
