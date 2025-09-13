drop table order_item;
drop table sub_order;
drop table pay_order;
drop table o_storage;

-- Create table
create table o_storage
(
  storage_id	BIGINT primary key not null auto_increment comment '库存主键',
  product_code    CHAR(10) not null comment '商品编号',
  option_code	VARCHAR(12) comment '附加选项编码，为后续需求变更预留',
  stock_num     DECIMAL(19) comment '库存量',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment='库存表';

create table pay_order
(
  pay_order_id BIGINT primary key not null auto_increment comment '支付单主键',
  pay_order_no VARCHAR(18) not null unique comment '支付单订单号',
  carriage_amt DECIMAL(20) comment '运费',
  payer_no     VARCHAR(24) comment '付款人',
  total_amt    DECIMAL(22) comment '订单总额（含运费）',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='支付单表';

  


create table sub_order
(
   order_no  VARCHAR(20) primary key not null comment '订单号，主键',
   pay_order_id  BIGINT comment '支付单主键',
   carriage_amt DECIMAL(20)comment '运费',
   total_amt    DECIMAL(22) comment '订单总额（含运费）',
   store_no      VARCHAR(32) not null comment '店铺号'
  
)comment='子订单表';
-- Create/Recreate primary, unique and foreign key constraints 


create table order_item
(
	item_id BIGINT primary key not null auto_increment comment '订单项主键',
	pay_order_id BIGINT comment '支付单号',
	order_no VARCHAR(22)  not null comment '订单单号',
	product_code CHAR(10)  not null comment '商品编号',
	option_code VARCHAR(12) comment '附加选项编码',
	product_price DECIMAL(22) not null comment '单价',
	product_num   DECIMAL(10) not null comment '商品数量'
) comment='订单项';

-- create index
CREATE INDEX idx_store_product_no ON o_storage(product_code);
CREATE UNIQUE INDEX idx_pay_order_payer_order_no ON pay_order(pay_order_no);
CREATE INDEX idx_pay_order_payer_no ON pay_order(payer_no);
CREATE INDEX idx_sub_order_order_no ON sub_order (pay_order_id);
CREATE INDEX idx_order_item_sub_id ON order_item (order_no);
CREATE INDEX idx_order_item_pay_order_id ON order_item (pay_order_id);
-- CREATE INDEX IDX_SUB_ORDER_STORE_NO ON sub_order (STORE_NO);
-- CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON sub_order(PRODUCT_ID);
