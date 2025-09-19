package com.xiaoke1256.orders.gateway.filters;

import com.xiaoke1256.orders.auth.encrypt.HMAC256;
import com.xiaoke1256.orders.common.RespCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private static  final Logger LOG = LoggerFactory.getLogger(AuthGatewayFilterFactory.class);

    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    @Override
    public List<String> shortcutFieldOrder() {
        return new ArrayList<>();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            LOG.info("RequestURI:"+request.getURI());
            try {
                String path = request.getURI().getPath();
                if(path.substring(path.indexOf("/",1)).startsWith("/login")){
                    return chain.filter(exchange);
                }
                String token = request.getHeaders().getFirst("Authorization");
                if(StringUtils.isEmpty(token)){
                    response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    DataBuffer bodyDataBuffer = response.bufferFactory().wrap(("{code:'" + RespCode.LOGIN_ERROR.getCode() + "',msg:'尚未登录'}").getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(bodyDataBuffer));
                }

                if(!loginTokenGenerator.verify(token)){
                    response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    DataBuffer bodyDataBuffer = response.bufferFactory().wrap(("{code:'"+ RespCode.LOGIN_ERROR.getCode()+"',msg:'token失效'}").getBytes(StandardCharsets.UTF_8));
                    return response.writeWith(Mono.just(bodyDataBuffer));
                }

                return chain.filter(exchange);
            } catch (Exception e) {
                e.printStackTrace();
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                response.setStatusCode(HttpStatus.BAD_GATEWAY);
                DataBuffer bodyDataBuffer = response.bufferFactory().wrap(("{code:'" + RespCode.OTHER_ERROR.getCode() + "',msg:'网关发生未知异常'}").getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(bodyDataBuffer));
            }
        });
    }

}
