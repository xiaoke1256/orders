# 3rdPay 项目 DDD 架构设计

## 1. 领域模型设计

### 1.1 聚合根

#### ThirdPayOrder（第三方支付订单）
- **标识**：orderNo (订单号)
- **属性**：orderId, orderNo, thirdPayerNo, thirdPayeeNo, merchantPayerNo, merchantPayeeNo, orderType, orderStatus, amt, merchantNo, merchantOrderNo, bussinessNo, incident, remark, insertTime, updateTime, finishTime
- **行为**：创建订单、支付成功、支付失败、订单过期、标记需人工处理

#### Merchant（商户）
- **标识**：merchantNo (商户编号)
- **属性**：merchantId, merchantNo, merchantName, defaultAccNo, publicKey, successReturnUrl, failReturnUrl, notifyUrl, insertTime, updateTime
- **行为**：验证签名、获取通知URL

#### HouseholdAcc（分户账）
- **标识**：accNo (账号)
- **属性**：accId, accNo, accName, balance, insertTime, updateTime
- **行为**：扣款、收款、查询余额

### 1.2 值对象

#### OrderNo
- 订单号值对象，确保订单号的唯一性和格式正确性

#### Money
- 金额值对象，封装金额操作和验证

#### AccountNo
- 账号值对象，确保账号格式正确

#### OrderStatus
- 订单状态枚举：
  - STATUS_ACCEPT (00-受理支付)
  - STATUS_FAIL (99-失败)
  - STATUS_SUCCESS (90-成功)
  - STATUS_EXPIRED (98-处理超时)
  - STATUS_NEED_MANNUAL (95-需人工处理)

#### OrderType
- 订单类型枚举：
  - TYPE_CONSUME (01-消费)
  - TYPE_REFUND (02-退货款)
  - TYPE_SETTLE (03-与平台方结算)
  - TYPE_FINANCE (04-理财)
  - TYPE_INTEREST (05-结息)
  - TYPE_LOAN (06-借款)
  - TYPE_REPAY (07-还款)
  - TYPE_OTHER (99-其他)

### 1.3 领域服务

#### PaymentDomainService
- 负责处理支付核心业务逻辑
- 协调订单创建和账户操作
- 处理支付成功/失败场景

#### OrderDomainService
- 管理订单生命周期
- 处理订单过期检查和处理
- 提供订单查询服务

#### AccountDomainService
- 管理账户余额操作
- 确保账户资金操作的一致性

### 1.4 仓储接口

#### ThirdPayOrderRepository
- 订单数据访问接口
- 提供保存、查询、更新订单状态等方法

#### MerchantRepository
- 商户数据访问接口
- 提供根据商户编号查询商户信息等方法

#### HouseholdAccRepository
- 账户数据访问接口
- 提供账户查询、余额更新等方法

### 1.5 领域事件

#### PaymentSuccessEvent
- 支付成功事件
- 触发后续通知商户等操作

#### PaymentFailEvent
- 支付失败事件
- 触发失败处理流程

#### OrderExpiredEvent
- 订单过期事件
- 触发超时处理流程

## 2. DDD 分层结构

### 2.1 接口层 (Interface Layer)
- 处理HTTP请求和响应
- 负责参数验证和数据转换
- 调用应用服务完成业务操作

### 2.2 应用层 (Application Layer)
- 协调领域对象完成业务流程
- 处理事务边界
- 发布领域事件

### 2.3 领域层 (Domain Layer)
- 核心业务逻辑和规则
- 包含聚合根、值对象、领域服务
- 定义领域事件

### 2.4 基础设施层 (Infrastructure Layer)
- 实现仓储接口
- 提供消息队列支持
- 实现其他技术基础设施

## 3. 技术实现方案

### 3.1 包结构设计
```
com.xiaoke1256.thirdpay
  ├── interface // 接口层
  │   └── rest  // REST API控制器
  ├── application // 应用层
  │   ├── service // 应用服务
  │   └── dto     // 应用层数据传输对象
  ├── domain // 领域层
  │   ├── model // 领域模型
  │   │   ├── aggregate // 聚合根
  │   │   ├── valueobject // 值对象
  │   │   └── event // 领域事件
  │   ├── repository // 仓储接口
  │   └── service // 领域服务
  └── infrastructure // 基础设施层
      ├── repository // 仓储实现
      ├── mq // 消息队列
      └── util // 工具类
```

### 3.2 关键模式应用
- **聚合模式**：将相关对象组合为聚合，通过聚合根进行访问
- **领域事件模式**：使用事件驱动架构实现解耦
- **仓储模式**：封装数据访问细节
- **工厂模式**：用于创建复杂领域对象
- **策略模式**：用于实现不同支付方式的处理策略

## 4. 重构计划

### 4.1 阶段一：建立基础架构
- 创建DDD分层目录结构
- 实现基础设施层支持
- 定义核心领域模型接口

### 4.2 阶段二：领域层重构
- 实现聚合根和值对象
- 开发领域服务
- 定义领域事件

### 4.3 阶段三：应用层重构
- 实现应用服务
- 协调领域对象交互
- 处理事务和事件发布

### 4.4 阶段四：接口层重构
- 调整控制器适配新的应用服务
- 优化参数验证和错误处理

### 4.5 阶段五：测试和优化
- 编写单元测试和集成测试
- 性能优化和问题修复
- 文档完善