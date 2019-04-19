create table THIRD_PAY_ORDER
(
  ORDER_ID BIGINT primary key not null auto_increment comment '订单主键',
  ORDER_NO VARCHAR(20) not null unique comment '订单号',
  payer_no VARCHAR(18) not null comment '付款方会员号(第三方平台的)',
  payee_no VARCHAR(18) not null comment '收款方会员号(第三方平台的)',
  order_type VARCHAR(2) not null comment '订单类型：01-消费;02-退货款;03-与平台方结算;04-理财;05-结息',
  order_status VARCHAR(2) not null comment '状态：00-受理支付;99-失败;90-成功',
  amt    DECIMAL(22) not null comment '支付额',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间',
  finish_time TIMESTAMP not null DEFAULT NOW() comment '订单处理完成的（含成功和失败）'
) comment='第三方支付记录表';

CREATE UNIQUE INDEX IDX_THIRD_PAY_ORDER_NO ON THIRD_PAY_ORDER(ORDER_NO);
