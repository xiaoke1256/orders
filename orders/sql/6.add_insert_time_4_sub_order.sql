alter table SUB_ORDER add insert_time TIMESTAMP not null DEFAULT NOW() comment '插入时间';

alter table SUB_ORDER add update_time TIMESTAMP not null DEFAULT NOW() comment '修改时间';
