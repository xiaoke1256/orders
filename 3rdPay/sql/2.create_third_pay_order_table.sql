create table third_pay_order
(
  order_id BIGINT primary key not null auto_increment comment '订单主键',
  order_no VARCHAR(20) not null unique comment '订单号',
  third_payer_no VARCHAR(18) not null comment '付款方会员号(第三方平台的)',
  third_payee_no VARCHAR(18) not null comment '收款方会员号(第三方平台的)',
  merchant_payer_no VARCHAR(18) not null comment '商户方的付款方账号',
  merchant_payee_no VARCHAR(18) not null comment '商户方的收款方账号',
  order_type VARCHAR(2) not null comment '订单类型：01-消费;02-退货款;03-与平台方结算;04-理财;05-结息;06-借款;07-还款;99-其他',
  order_status VARCHAR(2) not null comment '状态：00-受理支付;99-失败;90-成功;98-处理超时;95-需人工处理',
  amt    DECIMAL(22) not null comment '支付额',
  merchant_no VARCHAR(64) not null comment '接入商户号(目前只有orders)',
  merchant_order_no VARCHAR(25) comment '商户方的订单号',
  bussiness_no VARCHAR(25) comment '其他业务号',
  -- platform VARCHAR(64) not null comment '接入平台(目前只有orders)',
  -- palteform VARCHAR(64) not null comment '接入平台(目前只有orders)',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间',
  finish_time TIMESTAMP null comment '订单处理完成的（含成功和失败）'
) comment='第三方支付记录表';

-- /* 商户表 */
create table merchant
(
  merchant_id BIGINT primary key not null auto_increment comment '商户主键',
  merchant_no VARCHAR(64) not null unique comment '商户编号',
  merchant_name VARCHAR(64) not null comment '商户名称',
  default_acc_no VARCHAR(18) null comment '商户收款账号',
  public_key VARCHAR(1024) null comment '商户公钥',
  -- 支付受理成功回调页面
  success_return_url VARCHAR(256) null comment '支付受理成功回调页面',
  -- 支付受理失败回调页面
  fail_return_url VARCHAR(256) null comment '支付受理失败回调页面',
  -- 订单处理结果回调接口（既可以处理成功也可以处理失败）
  notify_url VARCHAR(256) null comment '订单处理结果回调接口（既可以处理成功也可以处理失败）',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='商户表';

-- /* 分户账 */
create table household_acc
(
  acc_id BIGINT primary key not null auto_increment comment '账户主键',
  acc_no VARCHAR(18) not null unique comment '账户号',
  acc_name VARCHAR(256) not null unique comment '账户名',
  balance    DECIMAL(22) not null comment '余额',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='分户账';

CREATE UNIQUE INDEX idx_third_pay_order_no ON third_pay_order(order_no);


insert into merchant
(merchant_no,merchant_name,default_acc_no,public_key,success_return_url,fail_return_url,notify_url,insert_time,update_time)
values('orders','orders平台账户', '000000000000000000',
'-----BEGIN PUBLIC KEY-----\nMIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAyhkesmLxghoGvAKoclrA0fWtEklOFrzZIdVtwp0xfkOm1h3pwmn4xi1PQUgLi4UPRM1GmKup3toqdp6zAzXfbG2coDvZ1NDdailm+sbBVSEFi2wQQ5ArH3RkuGEmPmAQjlDiNBlU7OI3W18fsjzI9q+ERpmsAelhDL7OgC/TbqBhhuoUlLCvEYNX+5+sU0yAULJhwAJm3SAzgFipla1wxM3CFFO9xr7nOg2kNZZD5P1ycB7STWpnA2RSsbQixZCNTVE73OMsvL3iOHS2379WKwOLob+rFvpV1hF+Zg10h8UggCOMIkQ1wdFblKADhD8dsrAPSklu0V4jew2bALJhwQzaxNoY4AhiVsECwNTaInuy5Whn9E+ltqnVIJXjuKvKJnX366u3yaIY2h03X8wB7Rzz4tZNqmBZNeLkUmiD3SMXgj1RF1eVJLfaHclVX0ChRxktqMRFewbgLvzvvN3h+1CuegyyrmBmhAg3b1SrrcgHFeVOS73zoCuWtZPFgK8VAgMBAAE=\n-----END PUBLIC KEY-----'
,'http://www.orders.com:8081/pay/callback_success'
,'http://www.orders.com:8081/pay/callback_fail'
,'http://www.orders.com:8081/pay/notify'
,NOW(),NOW());

insert into household_acc
(acc_no,acc_name,balance,insert_time,update_time)
values('000000000000000000','orders平台账户',0,NOW(),NOW());
