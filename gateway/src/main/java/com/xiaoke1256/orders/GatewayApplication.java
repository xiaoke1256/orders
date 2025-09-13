package com.xiaoke1256.orders;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(
        scanBasePackages={"com.xiaoke1256.orders","org.springframework.http.codec"},
        exclude = DataSourceAutoConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args){
        SpringApplication.run(GatewayApplication.class,args);
        //initFlowRules();
    }

//    private static void initFlowRules() {
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource("*"); // 资源名称,可以用通配符
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS); // 根据 QPS 限流
//        rule.setCount(1); // QPS 阈值【每秒只允许通过一个请求】
//        rule.setStrategy(RuleConstant.STRATEGY_DIRECT); // 调用关系限流策略【非必须设置】
//        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT); // 流控效果【非必须设置】
//        rule.setClusterMode(false); // 是否集群限流【非必须设置，默认非集群】
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);
//    }
}
