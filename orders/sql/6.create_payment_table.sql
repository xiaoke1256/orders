create table THIRD_PAY_ORDER
(
  ORDER_ID BIGINT primary key not null auto_increment comment '订单主键',
  ORDER_NO VARCHAR(20) not null unique comment '订单号',
  payer_no VARCHAR(18) not null comment '付款方会员号(第三方平台的)',
  payee_no VARCHAR(18) not null comment '收款方会员号(第三方平台的)',
  amt    DECIMAL(22) not null comment '支付额',
  incident VARCHAR(256) comment '事由',
  remark VARCHAR(256) comment '备注',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='第三方支付记录表';

CREATE UNIQUE INDEX IDX_THIRD_PAY_ORDER_NO ON THIRD_PAY_ORDER(ORDER_NO);
