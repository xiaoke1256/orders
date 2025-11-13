-- 与清算有关的业务表

create table settle_statemt
(
  settle_id BIGINT primary key not null auto_increment comment '结算单主键',
  settle_no VARCHAR(12) not null comment '结算单号，商铺号后两位+年月日+2位随机码',
  year char(4) not null comment '年份',
  month char(2) not null comment '月份',
  store_no VARCHAR(18) not null comment '商铺号',
  status char(1) not null comment '状态 0-待打款;1-完成打款;9-废除',
  total_amt DECIMAL(25) comment '订单总额',
  monthly_charge DECIMAL(20) comment '月租费',
  commission DECIMAL(20) comment '佣金',
  other_charge DECIMAL(20) comment '其他扣除',
  pending_payment DECIMAL(20) comment '待结款',
  already_paid DECIMAL(20) comment '已结款',
  memo varchar(512) comment '备忘',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
) comment='结算单表';

create table settle_item_order
(
  item_id BIGINT primary key not null auto_increment comment '结算单项主键',
  settle_id BIGINT not null comment '结算单主键',
  order_no VARCHAR(20) not null comment '订单号',
  total_amt DECIMAL(25) comment '订单总额',
  commission DECIMAL(20) comment '佣金',
  other_charge DECIMAL(20) comment '其他扣除',
  insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间'
 ) comment='结算单项(订单类)表';
 
CREATE UNIQUE INDEX idx_settle_statemt_no ON settle_statemt(settle_no);
CREATE INDEX idx_settle_item_order_settle_id ON settle_item_order(settle_id);