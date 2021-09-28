-- 店铺与会员的对应关系
create table store_member (
    store_member_id BIGINT auto_increment comment '主键',
    store_no VARCHAR(32) not null comment '商铺编号',
    account_no VARCHAR(32) not null comment '会员账号（登录时用）',
    role CHAR(2) comment '角色（1：店长；2：店员）',
    is_default_store CHAR(1) comment '是否默认商店',
    create_time TIMESTAMP not null  DEFAULT NOW() comment '创建时间',
    update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间',
    primary key(store_member_id)
) comment = '店员表';

CREATE UNIQUE INDEX IDX_STORE_NO_ACC_NO ON store_member(store_no,account_no);