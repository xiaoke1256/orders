create table PAYMENT_TXN
(
  PAYMENT_ID BIGINT primary key not null auto_increment comment '支付流水主键',
  payer_no VARCHAR(18) not null comment '付款方会员号(18个‘0’表示平台方)',
  payee_no VARCHAR(18) not null comment '收款方会员号(18个‘0’表示平台方)',
  pay_type char(3) not null comment '支付方式(淘宝，支付宝等)',
  amt    DECIMAL(22) not null comment '支付额',
  pay_order_no VARCHAR(18) comment '支付单号',
  sub_order_no VARCHAR(20) comment '订单号',
  THIRD_ORDER_NO VARCHAR(20) comment '第三方平台的订单号',
  REVERSE_FLG char(1) default '0' comment '冲正标记：“0:未冲正;1:已冲正”',
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

