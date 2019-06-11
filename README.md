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
以ordersUser用户登录，执行以下脚本，创建数据表：
```
use orders; 

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

```
alter table SUB_ORDER add insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间';
alter table SUB_ORDER add update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间';
alter table SUB_ORDER add receive_time TIMESTAMP null comment '买家收货时间';

update SUB_ORDER set receive_time = update_time where status = '4';-- 4表示待清算的。
```

```
create table LO_ORDER
(
  lo_order_id BIGINT primary key not null auto_increment comment '物流单主键',
  lo_order_no VARCHAR(18) not null comment '物流单号',
  company_code VARCHAR(4) not null comment '物流公司代码',
  carriage_amt DECIMAL(20) comment '运费',
  sub_order_no VARCHAR(20) not null comment '订单号',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='物流单表';

CREATE UNIQUE INDEX IDX_COMPANY_LO_ORDER_NO ON LO_ORDER(company_code,lo_order_no);
```

```
-- 与清算有关的业务表
create table SETTLE_STATEMT
(
  SETTLE_ID BIGINT primary key not null auto_increment comment '结算单主键',
  SETTLE_NO VARCHAR(12) not null comment '结算单号，商铺号后两位+年月日+2位随机码',
  YEAR char(4) not null comment '年份',
  MONTH char(2) not null comment '月份',
  STORE_NO VARCHAR(18) not null comment '商铺号',
  STATUS char(1) not null comment '状态 0-待打款;1-完成打款;9-废除',
  TOTAL_AMT DECIMAL(25) comment '订单总额',
  MONTHLY_CHARGE DECIMAL(20) comment '月租费',
  COMMISSION DECIMAL(20) comment '佣金',
  OTHER_CHARGE DECIMAL(20) comment '其他扣除',
  PENDING_PAYMENT DECIMAL(20) comment '待结款',
  ALREADY_PAID DECIMAL(20) comment '已结款',
  MEMO varchar(512) comment '备忘',
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='结算单表';

create table SETTLE_ITEM_ORDER
(
  ITEM_ID BIGINT primary key not null auto_increment comment '结算单项主键',
  SETTLE_ID BIGINT not null comment '结算单主键',
  ORDER_NO VARCHAR(20) not null comment '订单号',
  TOTAL_AMT DECIMAL(25) comment '订单总额',
  COMMISSION DECIMAL(20) comment '佣金',
  OTHER_CHARGE DECIMAL(20) comment '其他扣除',
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
 ) comment='结算单项(订单类)表';
 
CREATE UNIQUE INDEX IDX_SETTLE_STATEMT_NO ON SETTLE_STATEMT(SETTLE_NO);
CREATE INDEX IDX_SETTLE_ITEM_ORDER_SETTLE_ID ON SETTLE_ITEM_ORDER(SETTLE_ID);
```

切换回root用户，执行以下脚本，创建product数据库：
```
CREATE USER 'productUser'@'%' IDENTIFIED BY 'xiaoke_1256';
create schema product default character set utf8 collate utf8_general_ci;
GRANT ALL ON product.* TO 'productUser'@'%' IDENTIFIED BY 'xiaoke_1256';
```
用productUser用户登录MySql，再执行以下脚本：
```
create table product
(
  product_code    CHAR(10) primary key not null comment '商品编号，主键',
  product_name    VARCHAR(256) comment '商品名称',
  product_price DECIMAL(20) comment '价格(厘)',
  store_no      VARCHAR(32) comment '店铺号',
  product_status  CHAR(1) not null comment '状态：0=下架，1=上架',
  on_sale_time TIMESTAMP comment '上架时间，用于搜索引擎采集过滤(废除，靠update_time字段判断)',
  product_intro	VARCHAR(500) comment '简介',
  brand VARCHAR(128) comment '品牌',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment='商品表';

create table product_type(
	type_id VARCHAR(12) primary key not null comment '类型编号，主键',
	type_name VARCHAR(256) comment '类型名称',
	parent_type_id VARCHAR(12) comment '父类型编号',
	type_desc	VARCHAR(500) comment '类型描述',
	show_order INT comment '显示顺序',
	insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
    update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment = '商品分类';

create table product_param(
	param_id	BIGINT primary key auto_increment comment '主键',
	product_code    CHAR(10) not null comment '商品编号，外键',
	param_name   VARCHAR(128) comment '参数名',
	param_value  VARCHAR(256) comment '参数值',
	param_desc	VARCHAR(500) comment '参数描述',
	show_order INT comment '显示顺序'
) comment = '商品参数';

create table product_attached_option(
  option_code CHAR(12) primary key comment '选项编码，主键',
  product_code    CHAR(10) not null comment '商品编号，外键',
  option_type CHAR(1) not null  comment '选项类型：1=尺码，2=颜色，3=套装，4=型号，9=其他',
  option_name VARCHAR(128) comment '选项名称',
  option_value VARCHAR(128) comment '选项值，类型选颜色时，可以将颜色的编码值，保存在此。',
  price DECIMAL(19,2) comment '价格，指附加选项以后的价格',
  show_order INT comment '显示顺序'
) comment = '商品附加选项';

create table store
(
  store_no      VARCHAR(32) primary key not null comment '商铺编号，主键',
  store_name	VARCHAR(256) comment '商铺名称',
  store_intro	VARCHAR(500) comment '商铺简介',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
)comment = '商铺';

create table type_product_map(
	type_id VARCHAR(12) not null comment '类型编号，外键',
	product_code CHAR(10) not null comment '商品编号，外键'
)comment = '商品与类型的映射关系';

CREATE UNIQUE INDEX idx_type_product_map ON type_product_map(type_id,product_code); 
```

```
--大类：服装、箱包、玩具、家电、化妆品、运动、游戏、书、办公、美食
delete from product_type;
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('01','服装',null,null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0101','男装','01',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0102','女装','01',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0103','内衣','01',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0104','鞋袜','01',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('02','箱包',null,null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0201','钱包','02',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0202','旅行箱','02',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0203','双肩包','02',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0204','公文包','02',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0205','手提包','02',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('03','玩具',null,null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0301','积木','03',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030101','拼插','0301',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030102','建构','0301',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030103','磁力','0301',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0302','布偶','03',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0303','模型','03',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030301','船模','0303',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030302','航空模型','0303',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030303','摆件','0303',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0304','电玩','03',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04','电子',null,null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0401','家电','04',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040101','电视','0401',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040102','dvd','0401',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040103','洗衣机','0401',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040104','电冰箱','0401',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040105','扫地机','0401',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040108','吸尘器','0401',null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040106','厨电','0401',null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010601','电饭煲','040106',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010602','微波炉','040106',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010603','洗碗机','040106',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040109','取暖机','0401',null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0402','数码','04',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040201','手机','0402',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040202','平板电脑','0402',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040211','笔记本','0402',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040203','台式机','0402',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040204','相机/摄像','0402',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040205','游戏设备','0402',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040206','影音','0402',null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040207','鼠标','0402',null,7);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040208','键盘','0402',null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040209','U盘','0402',null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040210','手环','0402',null,10);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('05','化妆品',null,null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('06','运动',null,null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('07','游戏',null,null,7);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('08','书',null,null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('09','办公',null,null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('10','美食',null,null,10);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1001','零食','10',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1002','茶饮','10',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1004','餐饮','10',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1003','土特产','10',null,3);
```

```
alter table PRODUCT add IN_SECKILL CHAR(1) not null default '0' comment '是否正在进行秒杀活动。（是:1;否:0）';
alter table STORE add pay_type CHAR(3) null comment '支付方式，（目前全是3rdpay）';
alter table STORE add pay_account_no varchar(18) null comment '支付账号';
```
以root用户登录MySql，执行以下脚本，创建thirdpay数据库：
```
CREATE USER 'thirdpayUs'@'%' IDENTIFIED BY 'xiaoke_1256';
create schema thirdpay default character set utf8 collate utf8_general_ci;
GRANT ALL ON thirdpay.* TO 'thirdpayUser'@'%';
```
切换成thirdpayUser用户，执行以下脚本创建表：
```
create table THIRD_PAY_ORDER
(
  ORDER_ID BIGINT primary key not null auto_increment comment '订单主键',
  ORDER_NO VARCHAR(20) not null unique comment '订单号',
  payer_no VARCHAR(18) not null comment '付款方会员号(第三方平台的)',
  payee_no VARCHAR(18) not null comment '收款方会员号(第三方平台的)',
  order_type VARCHAR(2) not null comment '订单类型：01-消费;02-退货款;03-与平台方结算;04-理财;05-结息;06-借款;07-还款;99-其他',
  order_status VARCHAR(2) not null comment '状态：00-受理支付;99-失败;90-成功;98-处理超时;95-需人工处理',
  amt    DECIMAL(22) not null comment '支付额',
  palteform VARCHAR(64) not null comment '接入平台(目前只有orders)',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间',
  finish_time TIMESTAMP null DEFAULT NOW() comment '订单处理完成的（含成功和失败）'
) comment='第三方支付记录表';

CREATE UNIQUE INDEX IDX_THIRD_PAY_ORDER_NO ON THIRD_PAY_ORDER(ORDER_NO);

```

### 2、安装Elasticsearch

### 3、安装redis

### 4、安装zookeeper

### 5、配置各个服务器的域名

### 6、启动应用
