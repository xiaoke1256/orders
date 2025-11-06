create table payment_txn
(
  payment_id BIGINT primary key not null auto_increment comment '支付流水主键',
  payer_no VARCHAR(18) not null comment '付款方会员号(18个‘0’表示平台方)',
  payee_no VARCHAR(18) not null comment '收款方会员号(18个‘0’表示平台方)',
  pay_type char(3) not null comment '支付方式(001:支付宝,002:微信,003:3rdPay)',
  amt    DECIMAL(22) not null comment '支付额',
  pay_order_no VARCHAR(18) comment '支付单号',
  sub_order_no VARCHAR(20) comment '订单号',
  business_no VARCHAR(25) comment '其他业务号',
  third_order_no VARCHAR(20) comment '第三方平台的订单号',
  reverse_flg char(1) not null default '0' comment '冲正标记：“0:未冲正;1:已冲正”',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  deal_status char(1) comment '处理状态：“0:待处理;1:已处理分户账”',
  pay_status char(1) comment '支付状态：0:未支付;1:支付处理中;2:已支付;3:支付取消;4:支付超时;8:支付失败;9:结束',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='支付流水表；本表不处理订单状态，订单状态态由订单表处理';

create table household_acc_txn
(
  txn_id BIGINT primary key not null auto_increment comment '流水主键',
  payment_id BIGINT null comment '支付流水主键',
  acc_no VARCHAR(18) not null comment '账户号（即各个商户号，18个‘0’表示平台方）',
  acc_flg char(1) not null comment '借贷符号(+表示借，-表示贷)',
  pay_order_no VARCHAR(18) comment '支付单号',
  sub_order_no VARCHAR(20) comment '订单号',
  is_current char(1) not null comment '是否最后一笔流水',
  amt    DECIMAL(22) comment '支付额',
  cash_balance DECIMAL(22) comment '现金余额',
  remark VARCHAR(256) not null comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='分户账支付流水表';

