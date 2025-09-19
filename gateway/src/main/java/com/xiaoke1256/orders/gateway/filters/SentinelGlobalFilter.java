package com.xiaoke1256.orders.gateway.filters;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Sentinel全局限流过滤器
 * 实现全局限流和基于IP的限流功能
 */
@Component
public class SentinelGlobalFilter implements GlobalFilter, Ordered {

    // 全局限流资源名称
    private static final String GLOBAL_RESOURCE_NAME = "global_flow_control";
    // IP限流资源前缀
    private static final String IP_RESOURCE_PREFIX = "ip_limit_";

    @PostConstruct
    public void init() {
        List<FlowRule> rules = new ArrayList<>();
        
        // 1. 创建全局流控规则
        FlowRule globalRule = new FlowRule();
        globalRule.setResource(GLOBAL_RESOURCE_NAME);
        globalRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        globalRule.setCount(10000); // 全局QPS阈值
        globalRule.setClusterMode(false);
        globalRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        rules.add(globalRule);
        
        // 加载规则
        FlowRuleManager.loadRules(rules);
        System.out.println("Sentinel限流规则已初始化，全局资源名称：" + GLOBAL_RESOURCE_NAME + ", IP限流资源前缀：" + IP_RESOURCE_PREFIX);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("进入 Sentinel Filter.");
        try {
            // 1. 执行全局限流检查
            Entry globalEntry = SphU.entry(GLOBAL_RESOURCE_NAME);
            try {
                // 2. 获取客户端IP地址并执行IP限流检查
                String clientIp = getClientIp(exchange);
                String ipResource = IP_RESOURCE_PREFIX + clientIp;
                
                // 为每个IP动态创建限流规则（首次访问时）
                if (!FlowRuleManager.hasConfig(ipResource)) {
                    createIpFlowRule(ipResource);
                }
                
                Entry ipEntry = SphU.entry(ipResource);
                try {
                    System.out.println("请求通过Sentinel限流检查：" + exchange.getRequest().getPath() + ", IP：" + clientIp);
                    return chain.filter(exchange);
                } finally {
                    if (ipEntry != null) {
                        ipEntry.exit();
                    }
                }
            } finally {
                if (globalEntry != null) {
                    globalEntry.exit();
                }
            }
        } catch (BlockException e) {
            // 请求被限流时的处理
            String clientIp = getClientIp(exchange);
            System.out.println("请求被Sentinel限流：" + exchange.getRequest().getPath() + ", IP：" + clientIp + ", 原因：" + e.getClass().getSimpleName());
            // 设置响应状态码为429 Too Many Requests
            String message = "{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}";
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            // 返回限流响应
            return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8))));
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIp(ServerWebExchange exchange) {
        // 先尝试从X-Forwarded-For头获取IP（如果应用在代理后面）
        String xForwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For可能包含多个IP，第一个是非代理IP
            return xForwardedFor.split(",")[0].trim();
        }
        
        // 如果没有X-Forwarded-For头，则从RemoteAddress获取
        return Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                .map(address -> address.getAddress().getHostAddress())
                .orElse("unknown");
    }

    /**
     * 为指定IP创建限流规则
     */
    private void createIpFlowRule(String ipResource) {
        FlowRule ipRule = new FlowRule();
        ipRule.setResource(ipResource);
        ipRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        ipRule.setCount(50); // 每个IP的QPS阈值
        ipRule.setClusterMode(false);
        ipRule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
        
        // 获取已有的规则
        List<FlowRule> existingRules = FlowRuleManager.getRules();
        List<FlowRule> newRules = new ArrayList<>(existingRules);
        
        // 检查是否已存在该IP的规则
        boolean exists = newRules.stream().anyMatch(rule -> rule.getResource().equals(ipResource));
        if (!exists) {
            newRules.add(ipRule);
            // 更新规则
            FlowRuleManager.loadRules(newRules);
            //System.out.println("已为IP资源创建限流规则：" + ipResource);
        }
    }


    @Override
    public int getOrder() {
        // 设置过滤器优先级，确保在其他过滤器之前执行
        return -100;
    }
}
