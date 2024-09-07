
-- 大数据模型
create table BIG_DATA_MODEL(
  model_id BIGINT primary key auto_increment comment '主键',
  model_name VARCHAR(128)  comment '模型名称',
  model_desc VARCHAR(1024)  comment '模型描述',
  model_type char(2) comment '模型类型：01：商品分类，02：用户分类，03：商品推送',
  algorithm_type VARCHAR(16) comment '模型算法：如Kmeans',
  train_param varchar(1024) comment '模型训练参数（包括维度系数）,封装成json',
  file_content BLOB comment '模型文件（二进制）',
  class_define varchar(1024) comment '分类定义（聚集算法用），即分类的label和分类的名称的对应关系',
  create_time DATETIME not null DEFAULT NOW() comment '创建时间',
  create_by VARCHAR(32) comment '创建人'
)comment = '大数据模型' ENGINE=InnoDB CHARSET=utf8mb4;


-- 聚类算法的结果保存
-- 计算执行信息
create table BIG_DATA_CAL_EXEC_INFO(
    exec_id BIGINT primary key auto_increment comment '主键',
    model_id BIGINT comment '模型主键',
    object_type char(2) comment '对象类型：01：商品，02：用户',
    algorithm_type VARCHAR(16) comment '执行算法',
    execute_time DATETIME not null DEFAULT NOW() comment '执行时间'
);
-- 聚类
create table BIG_DATA_CLUSTER(
    cluster_id BIGINT primary key auto_increment comment '主键',
    exec_id  BIGINT not null comment '执行主键',
    label varchar(10) not null comment '（算法产生的）标签',
    label_name varchar(128) comment '标签名',
    insert_time DATETIME not null DEFAULT NOW() comment '创建时间'
);
-- 聚类与对象的映射
create table BIG_DATA_CLUSTER_OBJECT_MAP(
   map_id BIGINT primary key auto_increment comment '主键',
   exec_id BIGINT not null comment '执行主键',
   cluster_id BIGINT not null comment '聚类主键',
   object_type char(2) comment '对象类型：01：商品，02：用户',
   object_id BIGINT not null comment '对象主键',
   insert_time DATETIME not null DEFAULT NOW() comment '创建时间'
);