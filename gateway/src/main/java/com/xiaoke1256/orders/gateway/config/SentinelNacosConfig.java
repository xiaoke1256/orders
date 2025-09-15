package com.xiaoke1256.orders.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Sentinel Nacos数据源配置类
 * 负责从Nacos加载限流规则并实现规则的动态更新
 */
@Configuration
public class SentinelNacosConfig implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(SentinelNacosConfig.class);

    @Value("${spring.cloud.sentinel.datasource.ds.nacos.server-addr}")
    private String serverAddr;

    @Value("${spring.cloud.sentinel.datasource.ds.nacos.dataId}")
    private String dataId;

    @Value("${spring.cloud.sentinel.datasource.ds.nacos.groupId}")
    private String groupId;

    /**
     * 初始化Nacos数据源
     */
    @PostConstruct
    public void init() {
        logger.info("=== 开始初始化Sentinel Nacos数据源 ===");
        logger.info("Nacos配置: serverAddr={}, dataId={}, groupId={}", serverAddr, dataId, groupId);

        // 初始化网关限流规则数据源
        initGatewayRulesDataSource();

        // 初始化API分组
        //initApiDefinitions();

        logger.info("=== Sentinel Nacos数据源初始化完成 ===");
    }

    /**
     * 初始化网关限流规则数据源
     */
    private void initGatewayRulesDataSource() {
        try {
            // 创建Nacos数据源，用于获取网关限流规则
            ReadableDataSource<String, Set<GatewayFlowRule>> gatewayRuleDataSource = new NacosDataSource<>(
                    serverAddr, groupId, dataId,
                    source -> JSON.parseObject(source, new TypeReference<Set<GatewayFlowRule>>() {})
            );

            // 注册数据源到GatewayRuleManager，实现规则的动态更新
            GatewayRuleManager.register2Property(gatewayRuleDataSource.getProperty());

            logger.info("成功注册Sentinel网关限流规则数据源到Nacos");
        } catch (Exception e) {
            logger.error("注册Sentinel网关限流规则数据源失败", e);
            // 如果注册失败，可以使用默认规则
            loadDefaultGatewayRules();
        }
    }

    /**
     * 初始化API分组定义
     */
    private void initApiDefinitions() {
        // 定义API分组
        Set<ApiDefinition> definitions = new HashSet<>();

        // 全局API分组，匹配所有路径
        ApiDefinition globalApi = new ApiDefinition("global_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        add(new ApiPathPredicateItem().setPattern("/**").setMatchStrategy(0));
                    }
                });

        // 商品服务API分组
        ApiDefinition productApi = new ApiDefinition("product_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        add(new ApiPathPredicateItem().setPattern("/product/**").setMatchStrategy(0));
                    }
                });

        // 订单服务API分组
        ApiDefinition ordersApi = new ApiDefinition("orders_api")
                .setPredicateItems(new HashSet<ApiPredicateItem>() {
                    {
                        add(new ApiPathPredicateItem().setPattern("/orders/**").setMatchStrategy(0));
                    }
                });

        definitions.add(globalApi);
        definitions.add(productApi);
        definitions.add(ordersApi);

        // 加载API分组定义
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
        logger.info("成功加载{}个API分组定义", definitions.size());
    }

    /**
     * 加载默认网关限流规则
     */
    private void loadDefaultGatewayRules() {
        logger.info("加载默认网关限流规则");

        Set<GatewayFlowRule> rules = new HashSet<>();

        // 为全局API设置默认限流规则
        GatewayFlowRule globalRule = new GatewayFlowRule("global_api")
                .setCount(10)  // QPS阈值
                .setIntervalSec(1);  // 统计时间窗口，单位是秒
        rules.add(globalRule);

        // 为商品服务设置限流规则
        GatewayFlowRule productRule = new GatewayFlowRule("product")
                .setCount(20)
                .setIntervalSec(1);
        rules.add(productRule);

        // 为订单服务设置限流规则
        GatewayFlowRule ordersRule = new GatewayFlowRule("orders")
                .setCount(15)
                .setIntervalSec(1);
        rules.add(ordersRule);

        // 加载规则
        GatewayRuleManager.loadRules(rules);
        logger.info("成功加载{}条默认网关限流规则", rules.size());
    }

    /**
     * 应用启动后执行的方法，确保规则正确加载
     */
    @Override
    public void run(ApplicationArguments args) {
        logger.info("应用启动完成，检查Sentinel规则加载情况");

        // 获取当前加载的网关规则数量
        int ruleCount = GatewayRuleManager.getRules().size();
        logger.info("当前加载的网关限流规则数量: {}", ruleCount);

        // 如果没有加载到规则，使用默认规则
        if (ruleCount == 0) {
            logger.warn("未从Nacos加载到限流规则，使用默认规则");
            loadDefaultGatewayRules();
        }
    }

    // Sentinel网关过滤器由SentinelSCGAutoConfiguration自动配置，无需手动声明

    /**
     * Sentinel初始化函数，用于在Sentinel启动时加载规则
     * 实现InitFunc接口，会被Sentinel自动调用
     */
    public static class SentinelDataSourceInitFunc implements InitFunc {
        @Override
        public void init() {
            // 这个初始化函数主要用于确保在Sentinel启动时能够正确加载规则
            // 实际的规则加载逻辑已经在SentinelNacosConfig类中实现
            LoggerFactory.getLogger(SentinelDataSourceInitFunc.class)
                    .info("Sentinel初始化函数执行，确保规则正确加载");
        }
    }
}