# 第三方支付模块 (ThirdPay Module)

基于领域驱动设计(DDD)的第三方支付模块，支持微信支付、支付宝等多种支付方式，提供完整的支付流程管理。

## 架构概览

本模块采用DDD架构设计，主要分为以下几层：

- **领域层(Domain)**: 核心业务逻辑，包含领域对象、领域服务、领域事件等
- **应用层(Application)**: 协调领域对象完成业务用例，提供应用服务接口
- **接口层(Interface)**: 处理外部请求，如REST API控制器
- **基础设施层(Infrastructure)**: 提供技术实现，如数据库访问、消息发送等

### 核心领域对象

- `ThirdPayOrder`: 第三方支付订单聚合根
- 值对象: `OrderNo`, `Amount`, `MerchantNo`, `ProductDesc`, `PaymentMethod`, `TransactionNo`
- 领域事件: `OrderPaidEvent`, `OrderFailedEvent`
- 领域服务: `PayService`

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Spring Boot 2.6+

### 安装与配置

1. 克隆代码库
   ```bash
   git clone <repository-url>
   cd orders/3rdPay
   ```

2. 配置数据库
   - 创建MySQL数据库
   - 修改`src/main/resources/application.yml`中的数据库连接配置

3. 配置支付参数
   - 根据`application.yml.example`创建`application.yml`
   - 填写微信支付和支付宝的相关配置参数

4. 构建项目
   ```bash
   mvn clean install
   ```

5. 运行应用
   ```bash
   mvn spring-boot:run
   ```

## API文档

### REST API接口

#### 1. 创建支付订单

**URL**: `/api/v1/payment/create`
**Method**: `POST`
**Request Body**:
```json
{
  "merchantNo": "MERCHANT001",
  "amount": 100.00,
  "productDesc": "测试商品",
  "paymentMethod": "WECHAT_PAY"
}
```

**Response**:
```json
{
  "orderNo": "ORDER20240101000001",
  "amount": 100.00,
  "status": "CREATED",
  "payUrl": "https://pay.example.com/...",
  "createTime": "2024-01-01T10:00:00.000+00:00",
  "expireTime": "2024-01-01T10:30:00.000+00:00"
}
```

#### 2. 查询订单

**URL**: `/api/v1/payment/orders/{orderNo}`
**Method**: `GET`

**Response**:
```json
{
  "orderNo": "ORDER20240101000001",
  "amount": 100.00,
  "status": "PAID",
  "createTime": "2024-01-01T10:00:00.000+00:00",
  "expireTime": "2024-01-01T10:30:00.000+00:00",
  "payTime": "2024-01-01T10:15:00.000+00:00"
}
```

#### 3. 支付回调

**URL**: `/api/v1/payment/callback`
**Method**: `POST`
**Request Body**:
```json
{
  "orderNo": "ORDER20240101000001",
  "status": "SUCCESS",
  "transactionNo": "TX1234567890"
}
```

**Response**:
```json
{
  "code": "SUCCESS",
  "message": "处理成功"
}
```

#### 4. 关闭过期订单

**URL**: `/api/v1/payment/close-expired`
**Method**: `POST`

**Response**:
```json
{
  "code": "SUCCESS",
  "message": "处理成功"
}
```

## 事件机制

本模块使用领域事件机制实现业务解耦，主要事件包括：

- **OrderPaidEvent**: 订单支付成功事件
- **OrderFailedEvent**: 订单支付失败事件

事件监听器：
- `OrderPaidEventListener`: 处理支付成功后的业务，如通知商户、更新库存等
- `OrderFailedEventListener`: 处理支付失败后的业务，如通知商户、释放资源等

## 事务管理

- 使用Spring的`@Transactional`注解管理事务
- 关键业务操作都在事务内执行，确保数据一致性
- 事件发布采用异步方式，避免影响主事务

## 安全性

- API请求签名验证
- 敏感信息加密存储
- 支付金额验证和边界检查
- SQL注入防护

## 测试

本模块包含以下测试：

- 单元测试: 测试领域对象、领域服务的核心业务逻辑
- 集成测试: 测试完整的支付流程，从创建订单到支付回调

运行测试：
```bash
mvn test
```

## 监控与日志

- 关键操作日志记录
- 支付流程日志跟踪
- 异常日志详细记录
- 支持对接ELK等日志分析系统

## 扩展指南

### 添加新的支付方式

1. 在`PaymentMethod`值对象中添加新的支付方式
2. 实现对应的支付适配器
3. 在应用服务中添加相应的处理逻辑

### 扩展事件处理

1. 创建新的领域事件类，实现`DomainEvent`接口
2. 在领域对象中发布新事件
3. 创建事件监听器处理新事件

## 许可证

[MIT License](LICENSE)

## 联系方式

如有问题或建议，请联系技术团队：
- Email: tech@example.com
- 项目地址: <repository-url>