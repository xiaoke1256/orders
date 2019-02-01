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
	pay_order_id BIGINT  not null comment '支付单号',
	order_no VARCHAR(22)  not null comment '订单单号',
	product_code CHAR(10)  not null comment '商品编号',
	option_code	VARCHAR(12) comment '附加选项编码',
	product_num   DECIMAL(10) comment '商品数量'
	
) comment='订单项' ;

--create index
CREATE INDEX IDX_STORE_PRODUCT_NO ON O_STORAGE(product_code);
CREATE INDEX IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE INDEX IDX_SUB_ORDER_ORDER_NO ON SUB_ORDER (PAY_ORDER_ID);
CREATE INDEX IDX_ORDER_ITEM_SUB_ID ON ORDER_ITEM (ORDER_NO);
CREATE INDEX IDX_ORDER_ITEM_ORDER_NO ON ORDER_ITEM (PAY_ORDER_ID);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
