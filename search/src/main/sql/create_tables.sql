--use product;
create table es_collect_logs(
	log_id BIGINT primary key auto_increment comment '主键',
	modify_count INT not null comment '修改记录数',
	new_count INT not null comment '新增记录数',
	exe_time TIMESTAMP not null comment '执行时间',
	insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间'
)comment = '搜索引擎，收录时的日志';