package com.xiaoke1256.orders.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sentinel网关限流配置类
 * 配置全局限流规则和路由级别的限流规则
 */
@Configuration
public class SentinelGatewayConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SentinelGatewayConfig.class);

    // 从配置文件中读取全局限流阈值
    @Value("${spring.cloud.sentinel.gateway.global-qps:1}")
    private int globalQps;

    /**
     * 初始化限流规则
     */
    @PostConstruct
    public void initGatewayRules() {
        logger.info("=== 开始初始化Sentinel网关限流规则 ===");
        // 初始化API分组
        initCustomizedApis();

        // 初始化限流异常处理器
        initBlockRequestHandler();

        logger.info("=== Sentinel网关限流规则初始化完成 ===");
    }

    /**
     * 初始化API分组
     */
    private void initCustomizedApis() {
        logger.info("初始化API分组配置...");
        Set<ApiDefinition> definitions = new HashSet<>();

        // 定义全局API模式，匹配所有路径
        ApiDefinition globalApi = new ApiDefinition("global_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
                    // 使用精确匹配策略确保所有路径都被匹配
                    add(new ApiPathPredicateItem().setPattern("/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                }});

        logger.info("创建API分组: 'global_api'，模式=/**, 匹配策略=前缀匹配");

        definitions.add(globalApi);
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);

        // 验证API分组是否正确加载
        Set<ApiDefinition> loadedApis = GatewayApiDefinitionManager.getApiDefinitions();
        logger.info("成功加载{}个API分组", loadedApis.size());
        for (ApiDefinition api : loadedApis) {
            logger.info("已加载API分组: '{}'", api.getApiName());
        }

        logger.info("API分组初始化完成");
    }

    /**
     * 初始化限流异常处理器
     */
    private void initBlockRequestHandler() {
        logger.info("初始化限流异常处理器...");

        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange exchange, Throwable t) {
                // 记录限流请求日志
                String path = exchange.getRequest().getURI().getPath();
                logger.info("请求被限流: {}, 用户IP: {}", path, getClientIp(exchange));

                // 构建限流响应
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}"));
            }
        });

        logger.info("限流异常处理器初始化完成，限流时返回429状态码和JSON响应");
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(ServerWebExchange exchange) {
        List<String> xForwardedFor = exchange.getRequest().getHeaders().get("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.get(0);
        }
        return exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
    }
}