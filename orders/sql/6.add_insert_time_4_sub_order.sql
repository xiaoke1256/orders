alter table SUB_ORDER add insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间';

alter table SUB_ORDER add update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间';

alter table SUB_ORDER add receive_time TIMESTAMP null comment '买家收货时间';


update SUB_ORDER set receive_time = update_time where status = '4';-- 4表示待清算的。