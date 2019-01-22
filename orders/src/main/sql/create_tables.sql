drop table SUB_ORDER;
drop table PAY_ORDER;
drop table O_STORAGE;

-- Create table
create table O_STORAGE
(
  storage_id	BIGINT primary key not null auto_increment,
  product_code    CHAR(10) not null comment '商品编号',
  option_code	VARCHAR(12) comment '附加选项编码',
  stock_num     DECIMAL(19) comment '库存量',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment='库存表';

create table PAY_ORDER
(
  pay_order_id BIGINT primary key not null auto_increment,
  pay_order_no VARCHAR(22) not null,
  carriage_amt DECIMAL(19,2),
  payer_no     VARCHAR(24),
  total_amt    DECIMAL(19,2),
  insert_time TIMESTAMP not null DEFAULT NOW(),
  update_time TIMESTAMP not null DEFAULT NOW()
) comment='支付单表';

  


create table SUB_ORDER
(
  sub_order_id  BIGINT primary key not null auto_increment,
  product_num   DECIMAL(10),
  product_price DECIMAL(19),
  store_no      VARCHAR(32) not null,
  pay_order_id  BIGINT,
  product_code    CHAR(10)
);
-- Create/Recreate primary, unique and foreign key constraints 

--create index
CREATE INDEX IDX_PRODUCT_STORE_NO ON PRODUCT(STORE_NO);
CREATE INDEX IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE INDEX IDX_SUB_ORDER_ORDER_ID ON SUB_ORDER (PAY_ORDER_ID);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
