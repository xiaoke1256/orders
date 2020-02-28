create table product
(
  product_code    CHAR(10) primary key not null comment '商品编号，主键',
  product_name    VARCHAR(256) comment '商品名称',
  product_price DECIMAL(20) comment '价格(厘)',
  store_no      VARCHAR(32) comment '店铺号',
  product_status  CHAR(1) not null comment '状态：0=下架，1=上架',
  on_sale_time TIMESTAMP comment '上架时间，用于搜索引擎采集过滤(废除，靠update_time字段判断)',
  product_intro	VARCHAR(500) comment '简介',
  brand VARCHAR(128) comment '品牌',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment='商品表';

create table product_type(
	type_id VARCHAR(12) primary key not null comment '类型编号，主键',
	type_name VARCHAR(256) comment '类型名称',
	parent_type_id VARCHAR(12) comment '父类型编号',
	type_desc	VARCHAR(500) comment '类型描述',
	show_order INT comment '显示顺序',
	insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
    update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
) comment = '商品分类';

create table product_param(
	param_id	BIGINT primary key auto_increment comment '主键',
	product_code    CHAR(10) not null comment '商品编号，外键',
	param_name   VARCHAR(128) comment '参数名',
	param_value  VARCHAR(256) comment '参数值',
	param_desc	VARCHAR(500) comment '参数描述',
	show_order INT comment '显示顺序'
) comment = '商品参数';

create table product_attached_option(
  option_code CHAR(12) primary key comment '选项编码，主键',
  product_code    CHAR(10) not null comment '商品编号，外键',
  option_type CHAR(1) not null  comment '选项类型：1=尺码，2=颜色，3=套装，4=型号，9=其他',
  option_name VARCHAR(128) comment '选项名称',
  option_value VARCHAR(128) comment '选项值，类型选颜色时，可以将颜色的编码值，保存在此。',
  price DECIMAL(19,2) comment '价格，指附加选项以后的价格',
  show_order INT comment '显示顺序'
) comment = '商品附加选项';

create table store
(
  store_no      VARCHAR(32) primary key not null comment '商铺编号，主键',
  store_name	VARCHAR(256) comment '商铺名称',
  store_intro	VARCHAR(500) comment '商铺简介',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
)comment = '商铺';

create table type_product_map(
	type_id VARCHAR(12) not null comment '类型编号，外键',
	product_code CHAR(10) not null comment '商品编号，外键'
)comment = '商品与类型的映射关系';

CREATE UNIQUE INDEX idx_type_product_map ON type_product_map(type_id,product_code); 

create table member(
  member_id BIGINT primary key auto_increment comment '会员主键',
  account_no VARCHAR(32) not null comment '会员账号（登录时用）',
  nick_name VARCHAR(128) comment '昵称',
  gender char(1) comment '性别：M 男；F 女',
  intro VARCHAR(500) comment '自我介绍',
  email VARCHAR(256) comment '绑定邮箱',
  mobile_phone VARCHAR(32) comment '移动电话号',
  insert_time TIMESTAMP not null  DEFAULT NOW() comment '插入时间',
  update_time TIMESTAMP not null  DEFAULT NOW() comment '修改时间'
)comment = '会员表';
CREATE UNIQUE INDEX idx_member_no ON member(account_no); 