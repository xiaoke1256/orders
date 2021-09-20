-- 店铺与会员的对应关系
create table store_member_map (
    store_no VARCHAR(32) comment '商铺编号',
    member_id BIGINT comment '会员编号',
    role CHAR(2) comment '角色（1：店长；2：店员）',
    is_default_store CHAR(1) comment '是否默认商店',
    create_time TIMESTAMP not null  DEFAULT NOW() comment '创建时间',
    update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间',
    primary key(store_no,member_id)
) comment = '店铺与成员的对应关系';