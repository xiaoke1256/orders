alter table PAY_ORDER add STATUS char(1) default '0' comment '0:未支付;1:已支付';

alter table SUB_ORDER add STATUS char(1) default '0' comment '0:待支付;1:待确认;2:待发货;3:送货中;4:待清算;5:待打款;9:结束';
