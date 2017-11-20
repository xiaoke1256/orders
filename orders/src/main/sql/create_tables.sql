drop table SUB_ORDER;
drop table PAY_ORDER;
drop table PRODUCT;

-- Create table
create table PRODUCT
(
  product_code    CHAR(10) not null,
  product_price NUMBER(19,2),
  stock_num     NUMBER(19),
  store_no      VARCHAR2(32),
  insert_time date not null,
  update_time date not null
);

alter table PRODUCT
  add primary key (product_code);
  

create table PAY_ORDER
(
  pay_order_id NUMBER(10) not null,
  pay_order_no VARCHAR2(22) not null,
  carriage_amt NUMBER(19,2),
  payer_no     VARCHAR2(24),
  total_amt    NUMBER(19,2),
  insert_time date not null,
  update_time date not null
);

alter table PAY_ORDER
  add primary key (pay_order_id);
  


create table SUB_ORDER
(
  sub_order_id  NUMBER(10) not null,
  product_num   NUMBER(10),
  product_price NUMBER(19),
  store_no      VARCHAR2(32) not null,
  pay_order_id  NUMBER(10),
  product_code    CHAR(6)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table SUB_ORDER
  add primary key (SUB_ORDER_ID);
  
--create sequense
create sequence SEQ_PAYODER
minvalue 1000
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 100;

create sequence SEQ_SUBODER
minvalue 1000
maxvalue 999999999999999999999999999
start with 1
increment by 1
cache 100;
  
--create index
CREATE INDEX IDX_PRODUCT_STORE_NO ON PRODUCT(STORE_NO);
CREATE INDEX IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE INDEX IDX_SUB_ORDER_ORDER_ID ON SUB_ORDER (PAY_ORDER_ID);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
