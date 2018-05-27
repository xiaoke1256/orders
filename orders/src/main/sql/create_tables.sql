drop table SUB_ORDER;
drop table PAY_ORDER;
drop table PRODUCT;

-- Create table
create table PRODUCT
(
  product_code    CHAR(10) primary key not null,
  product_price DECIMAL(19,2),
  stock_num     DECIMAL(19),
  store_no      VARCHAR(32),
  insert_time TIMESTAMP not null  DEFAULT NOW(),
  update_time TIMESTAMP not null  DEFAULT NOW()
);

create table PAY_ORDER
(
  pay_order_id INT(10) primary key not null auto_increment,
  pay_order_no VARCHAR(22) not null,
  carriage_amt DECIMAL(19,2),
  payer_no     VARCHAR(24),
  total_amt    DECIMAL(19,2),
  insert_time TIMESTAMP not null DEFAULT NOW(),
  update_time TIMESTAMP not null DEFAULT NOW()
);

  


create table SUB_ORDER
(
  sub_order_id  INT(10) primary key not null auto_increment,
  product_num   DECIMAL(10),
  product_price DECIMAL(19),
  store_no      VARCHAR(32) not null,
  pay_order_id  DECIMAL(10),
  product_code    CHAR(10)
);
-- Create/Recreate primary, unique and foreign key constraints 

--create index
CREATE KEY IDX_PRODUCT_STORE_NO ON PRODUCT(STORE_NO);
CREATE KEY IDX_PAY_ORDER_PAYER_NO ON PAY_ORDER(PAYER_NO);
CREATE KEY IDX_SUB_ORDER_ORDER_ID ON SUB_ORDER (PAY_ORDER_ID);
--CREATE INDEX IDX_SUB_ORDER_STORE_NO ON SUB_ORDER (STORE_NO);
--CREATE INDEX IDX_SUB_ORDER_PRODUCT_ID ON SUB_ORDER(PRODUCT_ID);
