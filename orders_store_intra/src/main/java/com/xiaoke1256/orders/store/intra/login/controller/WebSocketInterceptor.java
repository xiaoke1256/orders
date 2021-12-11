package com.xiaoke1256.orders.store.intra.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebSocketInterceptor implements HandshakeInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if(serverHttpRequest instanceof ServletServerHttpRequest){
            HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            for(String paratmer:request.getParameterMap().keySet()){
                LOG.info("paratmer name: {}",paratmer);
                String value = request.getParameter(paratmer);
                LOG.info("paratmer value: {}",value);
            }
        }
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String sessionId = headers.getFirst("sessionid");
        if(sessionId==null){
            String cookies = headers.getFirst("cookie");
            String[] cookiesArray = cookies.split(";");
            for(String cookie:cookiesArray){
                String[] kv = cookie.trim().split("=");
                if("JSESSIONID".endsWith(kv[0])){
                    sessionId = kv[1];
                }
            }
        }
        LOG.info("sessionId is {}",sessionId);
        if(sessionId!=null && !"".equals(sessionId)){
            map.put("sessionId",sessionId);
            serverHttpResponse.setStatusCode(HttpStatus.OK);
            return true;
        }
        LOG.info("connect fail.");
        serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
