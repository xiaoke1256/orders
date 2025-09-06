--其实本字段应该放在营销模块中的
alter table product add in_seckill CHAR(1) not null default '0' comment '是否正在进行秒杀活动。（是:1;否:0）';