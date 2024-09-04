
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
