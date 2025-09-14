package com.xiaoke1256.orders.gateway.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Sentinel限流和熔断的异常处理类
 * 为@SentinelRestTemplate注解提供blockHandler和fallback方法实现
 */
public class SentinelBlockHandler {

    private static final Logger logger = LoggerFactory.getLogger(SentinelBlockHandler.class);

    /**
     * 处理Sentinel限流异常
     * 当请求被Sentinel限流时触发
     * 符合Sentinel要求的方法签名: [HttpRequest, byte[], ClientHttpRequestExecution, BlockException]
     */
    public static ClientHttpResponse handleBlock(HttpRequest request, byte[] body,
                                               ClientHttpRequestExecution execution, BlockException ex) {
        String url = request.getURI().toString();
        logger.warn("请求被限流: {}, 限流类型: {}", url, ex.getClass().getSimpleName());
        // 这里需要返回ClientHttpResponse对象，而不是ResponseEntity
        return new SentinelClientHttpResponse(429, "请求过于频繁，请稍后再试");
    }

    /**
     * 处理服务降级异常
     * 当服务不可用或响应超时触发降级时调用
     * 符合Sentinel要求的方法签名: [HttpRequest, byte[], ClientHttpRequestExecution, BlockException]
     */
    public static ClientHttpResponse handleFallback(HttpRequest request, byte[] body,
                                                 ClientHttpRequestExecution execution, BlockException ex) {
        String url = request.getURI().toString();
        logger.warn("服务降级: {}, 降级类型: {}", url, ex.getClass().getSimpleName());
        // 返回503状态码表示服务不可用
        return new SentinelClientHttpResponse(503, "服务暂时不可用，请稍后再试");
    }

    /**
     * 自定义ClientHttpResponse实现，用于Sentinel限流和降级响应
     */
    private static class SentinelClientHttpResponse implements ClientHttpResponse {
        
        private final int statusCode;
        private final String message;
        
        public SentinelClientHttpResponse(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }
        
        @Override
        public org.springframework.http.HttpStatus getStatusCode() throws IOException {
            return org.springframework.http.HttpStatus.valueOf(statusCode);
        }
        
        @Override
        public int getRawStatusCode() throws IOException {
            return statusCode;
        }
        
        @Override
        public String getStatusText() throws IOException {
            return getStatusCode().getReasonPhrase();
        }
        
        @Override
        public void close() {
            // 不需要特殊关闭操作
        }
        
        @Override
        public java.io.InputStream getBody() throws IOException {
            String responseBody = String.format("{\"code\":%d,\"message\":\"%s\"}", statusCode, message);
            return new java.io.ByteArrayInputStream(responseBody.getBytes("UTF-8"));
        }
        
        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            return headers;
        }
    }
}