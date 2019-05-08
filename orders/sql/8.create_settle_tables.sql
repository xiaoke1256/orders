-- 与清算有关的业务表

create table SETTLE_STATEMT
(
  SETTLE_ID BIGINT primary key not null auto_increment comment '结算单主键',
  SETTLE_NO VARCHAR(12) not null comment '结算单号，商铺号后两位+年月日+2位随机码',
  YEAR char(4) not null comment '年份',
  MONTH char(2) not null comment '月份',
  STORE_NO VARCHAR(18) not null comment '商铺号',
  STATUS char(1) not null comment '状态 0-待打款;1-完成打款;9-废除',
  TOTAL_AMT DECIMAL(25) comment '订单总额',
  MONTHLY_CHARGE DECIMAL(20) comment '月租费',
  COMMISSION DECIMAL(20) comment '佣金',
  OTHER_CHARGE DECIMAL(20) comment '其他扣除',
  PENDING_PAYMENT DECIMAL(20) comment '待结款',
  ALREADY_PAID DECIMAL(20) comment '已结款',
  MEMO varchar(512) comment '备忘',
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='结算单表';

create table SETTLE_ITEM_ORDER
(
  ITEM_ID BIGINT primary key not null auto_increment comment '结算单项主键',
  SETTLE_ID BIGINT not null comment '结算单主键',
  ORDER_NO VARCHAR(20) not null comment '订单号',
  TOTAL_AMT DECIMAL(25) comment '订单总额',
  COMMISSION DECIMAL(20) comment '佣金',
  OTHER_CHARGE DECIMAL(20) comment '其他扣除',
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
 ) comment='结算单项(订单类)表';
 
CREATE UNIQUE INDEX IDX_SETTLE_STATEMT_NO ON SETTLE_STATEMT(SETTLE_NO);
CREATE INDEX IDX_SETTLE_ITEM_ORDER_SETTLE_ID ON SETTLE_ITEM_ORDER(SETTLE_ID);