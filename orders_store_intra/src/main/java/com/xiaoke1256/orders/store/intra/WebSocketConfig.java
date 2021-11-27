package com.xiaoke1256.orders.store.intra;

import com.xiaoke1256.orders.store.intra.login.controller.LoginSocket;
import com.xiaoke1256.orders.store.intra.login.controller.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(loginSocket(), "/login").addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");
        registry.addHandler(loginSocket(), "/sockjs/login").addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*").withSockJS();
    }

    @Bean
    public LoginSocket loginSocket(){
        return new LoginSocket();
    }
}
