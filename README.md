# orders

## 一、概述

**一个简单的电子商务平台，探讨了以下情况：**
1. 高并发环境下订单的受理。
2. 商品搜索(ES)。
3. 高吞吐量的订单流转。  

**This is a simple e-commerce platform.We discuss these topics:**
1. Receiving orders in high concurrency environment.
2. Searching porducts(ES).
3. Order circulation in high throughput capacity.

## 二、流程图

### 购物流程
```
                               ┌-> 开启物流流程 -> 签收
                ┌----------┐   | 
  下订单 -> 支付 ┴> 确认订单 ┴> 出库  ┬> (确认收货) -┬> 清结算 -> 打款 -> 交易成功(结束) 
     |       |       |         |    |             |
  (取消)    (取消) (取消)    (取消)  └->(时限到期)--┘
     └---┬---┘       └---┬-----┘
         |               |
         |               V 
         |              退款
         |               |
         |               V
         └--------->交易取消(结束)
```

### 退货流程
```
                                    ┌-> 开启物流流程 -> 卖家签收
                                    |
买家发起退货 -> 卖家确认 -(同意)-> 买家发货 ┬-> (确认收货)-┬->打款给买家->对卖家进行清结算->退货成功(结束) 
                   |                ↑    └-> (时限到期)-┘
	     (拒绝或超时)            |
                   |                |
                   V                |
               平台方审核            |
                   ├----(同意退货)---┘
                   |
              (不同意退货)
                   |
                   V
             退货取消(结束)
	     
	     
                   ┌-> 开启物流流程 -> 卖家签收
                   |
卖家发起召回 -> 买家发货 ┬-> (确认收货)-┬->打款给买家->对卖家进行清结算->召回成功(结束) 
                   |   └-> (时限到期)-┘
	     (拒绝或超时)            
                   |                
                   V                
             召回取消(结束)
  ```

## 三、如何运行本项目

### 1、安装数据库

先安装一个MySql实例。

以root用户登录Mysql,执行以下脚本，创建orders数据库：
```
CREATE USER 'ordersUser'@'%' IDENTIFIED BY 'xiaoke_1256';
create schema orders default character set utf8 collate utf8_general_ci;
GRANT ALL ON orders.* TO 'ordersUser'@'%';
```
以ordersUser用户登录，执行以下SQL，创建表：
```
drop table ORDER_ITEM;
drop table SUB_ORDER;
drop table PAY_ORDER;
drop table O_STORAGE;

-- Create table
create table O_STORAGE
(
  storage_id	BIGINT primary key not null auto_increment comment '库存主键',
  product_code    CHAR(10) not null comment '商品编号',
  option_code	VARCHAR(12) comment '附加选项编码，为后续需求变更预留',
  stock_num     DECIMAL(19) comment '库存量',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment='库存表';

create table PAY_ORDER
(
  pay_order_id BIGINT primary key not null auto_increment comment '支付单主键',
  pay_order_no VARCHAR(18) not null unique comment '支付单订单号',
  carriage_amt DECIMAL(20) comment '运费',
  payer_no     VARCHAR(24) comment '付款人',
  total_amt    DECIMAL(22) comment '订单总额（含运费）',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='支付单表';

  


create table SUB_ORDER
(
   order_no  VARCHAR(20) primary key not null comment '订单号，主键',
   pay_order_id  BIGINT comment '支付单主键',
   carriage_amt DECIMAL(20)comment '运费',
   total_amt    DECIMAL(22) comment '订单总额（含运费）',
   store_no      VARCHAR(32) not null comment '店铺号'
  
)comment='子订单表';
-- Create/Recreate primary, unique and foreign key constraints 


create table ORDER_ITEM
(
	item_id BIGINT primary key not null auto_increment comment '订单项主键',
	pay_order_id BIGINT comment '支付单号',
	order_no VARCHAR(22)  not null comment '订单单号',
	product_code CHAR(10)  not null comment '商品编号',
	option_code	VARCHAR(12) comment '附加选项编码',
	product_price DECIMAL(22) not null comment '单价',
	product_num   DECIMAL(10) not null comment '商品数量'
	
) comment='订单项' ;

--create index
CREATE INDEX IDX_STORE_PRODUCT_NO ON O_STORAGE(product_code);
CREATE UNIQUE INDEX IDX_PAY_ORDER_PAYER_ORDER_NO ON PAY_ORDER(pay_order_no);
CREATE INDEX IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE INDEX IDX_SUB_ORDER_ORDER_NO ON SUB_ORDER (PAY_ORDER_ID);
CREATE INDEX IDX_ORDER_ITEM_SUB_ID ON ORDER_ITEM (ORDER_NO);
CREATE INDEX IDX_ORDER_ITEM_PAY_ORDER_ID ON ORDER_ITEM (PAY_ORDER_ID);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
```

```
alter table PAY_ORDER add STATUS char(1) default '0' comment '0:未支付;1:已支付';
alter table SUB_ORDER add STATUS char(1) default '0' comment '0:待支付;1:待确认;2:待发货;3:送货中;4:待清算;5:待打款;9:结束';
```

```
create table PAYMENT_TXN
(
  PAYMENT_ID BIGINT primary key not null auto_increment comment '支付流水主键',
  payer_no VARCHAR(18) not null comment '付款方会员号(18个‘0’表示平台方)',
  payee_no VARCHAR(18) not null comment '收款方会员号(18个‘0’表示平台方)',
  pay_type char(3) not null comment '支付方式(001:支付宝等,002:微信,003:3rdPay)',
  amt    DECIMAL(22) not null comment '支付额',
  pay_order_no VARCHAR(18) comment '支付单号',
  sub_order_no VARCHAR(20) comment '订单号',
  business_no VARCHAR(25) comment '其他业务号',
  THIRD_ORDER_NO VARCHAR(20) comment '第三方平台的订单号',
  REVERSE_FLG char(1) not null default '0' comment '冲正标记：“0:未冲正;1:已冲正”',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  deal_status char(1) comment '处理状态：“0:待处理;1:已处理分户账”',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='支付流水表';

create table HOUSEHOLD_ACC_TXN
(
  TXN_ID BIGINT primary key not null auto_increment comment '流水主键',
  ACC_NO VARCHAR(18) not null comment '账户号（即各个商户号，18个‘0’表示平台方）',
  ACC_FLG char(1) not null comment '借贷符号(+表示借，-表示贷)',
  pay_order_no VARCHAR(18) comment '支付单号',
  sub_order_no VARCHAR(20) comment '订单号',
  IS_CURRENT char(1) not null comment '是否最后一笔流水',
  amt    DECIMAL(22) comment '支付额',
  cash_balance DECIMAL(22) comment '现金余额',
  remark VARCHAR(256) not null comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='分户账支付流水表';
```

### 2、安装Elasticsearch

### 3、安装redis

### 4、安装zookeeper

### 5、配置各个服务器的域名

### 6、启动应用
