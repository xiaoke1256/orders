insert into store (store_no,store_name) values ('0001','华硕旗舰店');

insert into store (store_no,store_name) values ('0002','弘基专卖店');

insert into store (store_no,store_name) values ('0006','风华电子商城');

insert into store (store_no,store_name) values ('0008','酷儿玩具店');



insert into product (product_code,product_name,product_price,product_status,store_no)values ('0001','华硕鼠标',45.8,'0','0001'); 
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002','弘基鼠标',60,'0','0002'); 

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0003','联想笔记本',6500,'0','0006'); 
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0004','戴尔笔记本',5500,'0','0006'); 

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006','儿童益智恐龙模型',145,'0','0008'); 

insert into product_param (product_code,param_name,param_value,show_order)values ('0006','适用年龄','5-7岁',1); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0006','产品材质','塑料/胶制',2); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0006','型号','幻影忍者',3); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0006','颜色','黄',4);

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0007','乐高积木巨龙战车',68,'0','0008'); 

insert into product_param (product_code,param_name,param_value,show_order)values ('0007','适用年龄','3-4岁',1); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0007','产品材质','木质',2); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0007','颜色','蓝、绿',4);


