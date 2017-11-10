drop table SUB_ORDER;
drop table PAY_ORDER;
drop table PRODUCT;


create table PAY_ORDER
(
  pay_order_no VARCHAR2(40 CHAR) not null,
  carriage_amt NUMBER(19,2),
  payer_no     VARCHAR2(24 CHAR),
  total_amt    NUMBER(19,2),
  insert_time date not null,
  update_time date not null
);

alter table PAY_ORDER
  add primary key (PAY_ORDER_NO);
  
-- Create table
create table PRODUCT
(
  product_id    VARCHAR2(32 CHAR) not null,
  product_price NUMBER(19,2),
  stock_num     NUMBER(19),
  store_no      VARCHAR2(32 CHAR),
  insert_time date not null,
  update_time date not null
);

alter table PRODUCT
  add primary key (PRODUCT_ID);
  
  
-- Create table
create table SUB_ORDER
(
  sub_order_id  CHAR(32 CHAR) not null,
  product_num   NUMBER(10),
  product_price NUMBER(19),
  store_no      VARCHAR2(32 CHAR) not null,
  pay_order_no  VARCHAR2(40 CHAR),
  product_id    VARCHAR2(32 CHAR)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table SUB_ORDER
  add primary key (SUB_ORDER_ID);
  
CREATE INDEX IDX_PRODUCT_STORE_NO ON PRODUCT(STORE_NO);
CREATE INDEX IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE INDEX IDX_SUB_ORDER_ORDER_NO ON SUB_ORDER (PAY_ORDER_NO);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
