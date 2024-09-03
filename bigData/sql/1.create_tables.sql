
-- 大数据模型
create table BIG_DATA_MODEL(
  model_id BIGINT primary key auto_increment comment '主键',
  model_name  '模型名称',
  '模型描述',
  '模型类型：01：商品分类，02：用户分类，03：商品推送'
  '模型算法：如Kmeans'
  '模型训练参数（包括维度系数）'
  '模型文件（二进制）'
  '创建时间'
  '创建人'
)comment = '大数据模型' ENGINE=InnoDB CHARSET=utf8mb4;
