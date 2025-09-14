package com.xiaoke1256.orders.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;
import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Function;

/**
 * Sentinel网关限流配置类
 * 配置全局限流规则和路由级别的限流规则
 */
@Configuration
public class SentinelGatewayConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(SentinelGatewayConfig.class);
    
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;
    
    public SentinelGatewayConfig(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                               ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    // 从配置文件中读取全局限流阈值
    @Value("${spring.cloud.sentinel.gateway.global-qps:1}")
    private int globalQps;

    /**
     * 初始化限流规则
     */
    @PostConstruct
    public void initGatewayRules() {
        logger.info("=== 开始初始化Sentinel网关限流规则 ===");
        logger.info("从配置文件读取的全局限流阈值: {}", globalQps);
        
        // 定义网关限流规则
        Set<GatewayFlowRule> rules = new HashSet<>();
        
        // 设置全局流控规则 - 使用global_api API分组匹配所有请求
        GatewayFlowRule globalRule = new GatewayFlowRule("global_api")
                // 限流阈值从配置文件读取
                .setCount(globalQps)
                // 统计时间窗口，单位是秒
                .setIntervalSec(1)
                // 设置限流阈值类型为QPS
                .setGrade(RuleConstant.FLOW_GRADE_QPS)
                // 控制行为：快速失败
                .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
                // 匹配策略：自定义API名称模式
                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);

        logger.info("创建全局流控规则: 资源='global_api', QPS={}, 时间窗口=1秒, 控制行为=快速失败, 资源模式=CUSTOM_API_NAME", globalRule.getCount());

        rules.add(globalRule);

        // 加载限流规则
        GatewayRuleManager.loadRules(rules);

        // 验证规则是否正确加载
        Set<GatewayFlowRule> loadedRules = GatewayRuleManager.getRules();
        logger.info("成功加载{}条限流规则", loadedRules.size());
        for (GatewayFlowRule rule : loadedRules) {
            logger.info("已加载规则: 资源='{}', QPS={}, 时间窗口={}秒",
                        rule.getResource(), rule.getCount(), rule.getIntervalSec());
        }

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
                    // 添加具体路径模式作为备份
                    add(new ApiPathPredicateItem().setPattern("/store_intra/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                    add(new ApiPathPredicateItem().setPattern("/product/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
                    add(new ApiPathPredicateItem().setPattern("/orders/**").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
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

    /**
     * 提供一个内部REST控制器，用于检查Sentinel规则状态
     */
    @RestController
    public static class SentinelStatusController {

        @GetMapping("/sentinel/status")
        public Map<String, Object> getSentinelStatus() {
            Map<String, Object> status = new HashMap<>();

            // 获取限流规则
            Set<GatewayFlowRule> rules = GatewayRuleManager.getRules();
            status.put("rulesCount", rules.size());
            status.put("rules", rules);

            // 获取API分组
            Set<ApiDefinition> apis = GatewayApiDefinitionManager.getApiDefinitions();
            status.put("apisCount", apis.size());
            status.put("apis", apis);

            // 获取系统时间
            status.put("timestamp", System.currentTimeMillis());

            return status;
        }

        @GetMapping("/sentinel/reload-rules")
        public String reloadRules() {
            try {
                // 创建规则集
                Set<GatewayFlowRule> rules = new HashSet<>();
                
                // 全局规则
                GatewayFlowRule globalRule = new GatewayFlowRule("global_api")
                        .setCount(1) // 强制设置为1，确保测试效果
                        .setIntervalSec(1)
                        .setGrade(RuleConstant.FLOW_GRADE_QPS)
                        .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
                        .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME);
                rules.add(globalRule);

                GatewayRuleManager.loadRules(rules);
                logger.info("手动重载Sentinel规则成功");
                return "Sentinel规则重载成功，当前全局QPS限制为1";
            } catch (Exception e) {
                logger.error("重载Sentinel规则失败", e);
                return "Sentinel规则重载失败: " + e.getMessage();
            }
        }
    }
    
    /**
     * 注册自定义Sentinel网关过滤器
     * 使用自定义名称避免与SentinelSCGAutoConfiguration中的bean冲突
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter customSentinelGatewayFilter() {
        logger.info("注册自定义Sentinel网关过滤器");
        return new SentinelGatewayFilter();
    }
    
    /**
     * 注册自定义Sentinel网关异常处理器
     * 根据Sentinel版本要求，需要传入List<ViewResolver>和ServerCodecConfigurer参数
     * 使用自定义名称避免与SentinelSCGAutoConfiguration中的bean冲突
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebExceptionHandler customSentinelGatewayBlockExceptionHandler() {
        logger.info("注册自定义Sentinel网关异常处理器");
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }
}