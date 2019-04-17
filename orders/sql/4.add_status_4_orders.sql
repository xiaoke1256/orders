alter table PAY_ORDER add STATUS char(1) default '0' comment '0:未支付;1:已支付';

alter table SUB_ORDER add STATUS char(1) default '0' comment '0:待支付;1:待备货;2:待发货;3:待清算;4:待打款;9:结束';
