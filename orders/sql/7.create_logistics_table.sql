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