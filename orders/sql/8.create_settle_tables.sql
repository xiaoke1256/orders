--与清算有关的业务表
create table SETTLEMENT_STATEMENT
(
  SETTLEMENT_ID BIGINT primary key not null auto_increment comment '结算单主键',
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
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='结算单表';

create table SETTLEMENT_STATEMENT_ITEM
(
  ITEM_ID BIGINT primary key not null auto_increment comment '结算单项主键',
  ORDER_NO not null comment '订单号',
  TOTAL_AMT DECIMAL(25) comment '订单总额',
  COMMISSION DECIMAL(20) comment '佣金',
  OTHER_CHARGE DECIMAL(20) comment '其他扣除',
  INSERT_TIME TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  UPDATE_TIME TIMESTAMP not null DEFAULT NOW() comment '修改时间'
 ) comment='结算单项表';