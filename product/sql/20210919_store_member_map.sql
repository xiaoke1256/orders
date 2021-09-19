-- 店铺与成员的对应关系
create table store_member_map (
    store_no VARCHAR(32),
    member_id BIGINT,
    role CHAR(2),
    is_default CHAR(1),
    create_time TIMESTAMP not null  DEFAULT NOW(),
    update_time TIMESTAMP not null  DEFAULT NOW()
) comment = '店铺与成员的对应关系';