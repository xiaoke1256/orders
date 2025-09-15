# Sentinel限流规则持久化到Nacos配置指南

## 概述

本指南详细介绍了如何将Sentinel的限流规则持久化到Nacos配置中心，实现限流规则的动态管理和持久化存储。

## 实现原理

项目通过以下方式实现Sentinel限流规则的持久化：

1. 在`application.yml`中启用Sentinel的Nacos数据源配置
2. 创建`SentinelNacosConfig`配置类，负责从Nacos加载限流规则并实现规则的动态更新
3. 修改`SentinelGatewayConfig`类，专注于API分组定义和限流异常处理

## 配置步骤

### 1. 确保项目依赖正确

检查`pom.xml`文件，确保已添加以下依赖：

```xml
<!-- Sentinel核心依赖 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>

<!-- Sentinel网关适配 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-sentinel-gateway</artifactId>
</dependency>

<!-- Sentinel Nacos数据源 -->
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
    <version>1.7.1</version>
</dependency>

<!-- Nacos服务发现 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

### 2. 配置Sentinel Nacos数据源

在`application.yml`中已经配置了Sentinel的Nacos数据源：

```yaml
spring:
  cloud:
    sentinel:
      # 启用网关限流
      enabled: true
      # 取消控制台懒加载
      eager: true
      # 控制台配置
      transport:
        dashboard: localhost:1111
        port: 8719
      # 全局限流阈值配置
      gateway:
        global-qps: 1
      # 启用Nacos数据源，实现限流规则持久化
      datasource:
        ds:
          nacos:
            server-addr: localhost:8848
            dataId: gateway-sentinel-rules
            groupId: DEFAULT_GROUP
            rule-type: gw-flow
```

### 3. 在Nacos中创建配置

1. 启动Nacos服务器
2. 登录Nacos控制台（默认地址：http://localhost:8848/nacos）
3. 进入"配置管理" -> "配置列表"
4. 点击"+"按钮，新建配置
5. 填写配置信息：
   - **Data ID**: gateway-sentinel-rules
   - **Group**: DEFAULT_GROUP
   - **配置格式**: JSON
   - **配置内容**: 复制`sentinel-rules-example.json`文件中的内容

### 4. 配置项说明

Sentinel网关限流规则的主要配置项说明：

| 配置项 | 说明 | 默认值 | 备注 |
|-------|------|-------|------|
| resource | 资源名称 | - | 可以是网关路由ID或API分组名称 |
| resourceMode | 资源模式 | 0 | 0: 路由ID模式, 1: API分组模式 |
| count | 限流阈值 | - | QPS阈值 |
| intervalSec | 统计时间窗口 | 1 | 单位为秒 |
| controlBehavior | 流量控制行为 | 0 | 0: 快速失败, 1: 预热, 2: 排队等待 |
| burst | 突发流量处理 | 0 | 允许的突发请求数 |
| maxQueueingTimeoutMs | 最大排队等待时间 | 500 | 仅在controlBehavior=2时有效 |

## 动态更新规则

通过Nacos控制台可以动态更新限流规则，无需重启应用：

1. 登录Nacos控制台
2. 找到`gateway-sentinel-rules`配置
3. 编辑配置内容，修改限流规则
4. 点击"发布"按钮

规则发布后，Sentinel会自动感知并加载新的规则，实现限流规则的动态更新。

## 开发环境配置

在开发环境中，系统使用了独立的配置：

```yaml
spring:
  profiles: dev
  cloud:
    sentinel:
      transport:
        dashboard: localhost:1111
        datasource:
          ds:
            nacos:
              server-addr: localhost:8848
              dataId: gateway-sentinel-rules-dev
              groupId: DEFAULT_GROUP
              rule-type: gw-flow
```

开发环境使用`gateway-sentinel-rules-dev`作为dataId，避免与测试/生产环境的规则冲突。

## 示例规则

项目提供了`sentinel-rules-example.json`文件，包含了常用的限流规则示例：

- 全局API限流规则
- 各路由ID的限流规则
- API分组的限流规则
- 不同流量控制行为的配置

可以根据实际需求修改和扩展这些规则。

## 注意事项

1. 确保Nacos服务器正常运行，且应用能够访问到Nacos
2. 如果从Nacos加载规则失败，系统会自动加载默认规则
3. 修改规则时，注意JSON格式的正确性
4. 生产环境中，建议为不同服务设置合理的限流阈值
5. 可以通过Sentinel控制台监控限流情况

## 相关类说明

- `SentinelNacosConfig`：负责从Nacos加载限流规则并实现动态更新
- `SentinelGatewayConfig`：负责API分组定义和限流异常处理
- `GatewayApplication`：应用入口类，确保配置类被正确加载