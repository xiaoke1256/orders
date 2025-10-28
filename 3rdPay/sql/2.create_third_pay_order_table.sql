create table third_pay_order
(
  order_id BIGINT primary key not null auto_increment comment '订单主键',
  order_no VARCHAR(20) not null unique comment '订单号',
  payer_no VARCHAR(18) not null comment '付款方会员号(第三方平台的)',
  payee_no VARCHAR(18) not null comment '收款方会员号(第三方平台的)',
  order_type VARCHAR(2) not null comment '订单类型：01-消费;02-退货款;03-与平台方结算;04-理财;05-结息;06-借款;07-还款;99-其他',
  order_status VARCHAR(2) not null comment '状态：00-受理支付;99-失败;90-成功;98-处理超时;95-需人工处理',
  amt    DECIMAL(22) not null comment '支付额',
  platform VARCHAR(64) not null comment '接入平台(目前只有orders)',
  -- palteform VARCHAR(64) not null comment '接入平台(目前只有orders)',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间',
  finish_time TIMESTAMP null DEFAULT NOW() comment '订单处理完成的（含成功和失败）'
) comment='第三方支付记录表';

-- /* 分户账 */
create table household_acc
(
  acc_id BIGINT primary key not null auto_increment comment '流水主键',
  payment_id BIGINT null comment '支付流水主键',
  acc_no VARCHAR(18) not null comment '账户号（即各个商户号，18个‘0’表示平台方）',
  acc_flg char(1) not null comment '借贷符号(+表示借，-表示贷)',
  pay_order_no VARCHAR(18) comment '支付单号',
)

CREATE UNIQUE INDEX idx_third_pay_order_no ON third_pay_order(order_no);
