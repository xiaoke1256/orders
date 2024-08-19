-- 测试环境模拟数据
delete from store;
insert into store (store_no,store_name) values ('0001','华硕旗舰店');
insert into store (store_no,store_name) values ('0002','弘基专卖店');
insert into store (store_no,store_name) values ('0006','风华电子商城');
insert into store (store_no,store_name) values ('0008','酷儿玩具店');
insert into store (store_no,store_name) values ('2018100009','天天数码城');
insert into store (store_no,store_name) values ('2018100010','青青家电城');
insert into store (store_no,store_name) values ('2018100011','阳阳百货');
insert into store (store_no,store_name) values ('2018100012','莉莉服装店');
insert into store (store_no,store_name) values ('2018100013','羊族百货');
insert into store (store_no,store_name) values ('2018100014','巧虎乐园');
insert into store (store_no,store_name) values ('2018100015','老沪箱包馆');
insert into store (store_no,store_name) values ('2018100016','亮亮服装城');
insert into store (store_no,store_name) values ('2018100017','美至车模');
insert into store (store_no,store_name) values ('2018100018','和丰家居城');
insert into store (store_no,store_name,store_intro) values ('2018100058','林智生活商城','家电');
insert into store (store_no,store_name,store_intro) values ('2018100019','世纪百城家电',null);
insert into store (store_no,store_name,store_intro) values ('2018100020','美的专卖店',null);
insert into store (store_no,store_name,store_intro) values ('2018100021','小米专卖店',null);
insert into store (store_no,store_name,store_intro) values ('2018100022','创维数码广场',null);
insert into store (store_no,store_name,store_intro) values ('2018100023','索尼数码专营店',null);
insert into store (store_no,store_name,store_intro) values ('2018100024','酷动城','运动');
insert into store (store_no,store_name,store_intro) values ('2018100025','迪卡侬官方旗舰店','运动');
insert into store (store_no,store_name,store_intro) values ('2018100026','北山狼旗舰店','户外');
insert into store (store_no,store_name,store_intro) values ('2018100027','优衣库官方旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100028','李宁专卖店','运动');
insert into store (store_no,store_name,store_intro) values ('2018100029','超士运动专营店','运动');
insert into store (store_no,store_name,store_intro) values ('2018100030','9377页游大全','游戏点券');
insert into store (store_no,store_name,store_intro) values ('2018100031','17173游戏王','游戏点券');
insert into store (store_no,store_name,store_intro) values ('2018100032','意气书店',null);
insert into store (store_no,store_name,store_intro) values ('2018100033','博航丽鑫图书专营店',null);
insert into store (store_no,store_name,store_intro) values ('2018100034','北京联合出版社',null);
insert into store (store_no,store_name,store_intro) values ('2018100035','首都文史书店',null);
insert into store (store_no,store_name,store_intro) values ('2018100036','九九图书专营店',null);
insert into store (store_no,store_name,store_intro) values ('2018100037','三只松鼠专卖店',null);
insert into store (store_no,store_name,store_intro) values ('2018100038','品味轩食品专卖店',null);
insert into store (store_no,store_name,store_intro) values ('2018100039','老生鲜旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100040','林南食品专营店',null);
insert into store (store_no,store_name,store_intro) values ('2018100041','一方食品旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100042','盛师傅旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100043','知味观糕点',null);
insert into store (store_no,store_name,store_intro) values ('2018100044','静安寺素月饼',null);
insert into store (store_no,store_name,store_intro) values ('2018100045','一杯香茗旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100046','花香四季旗舰店','茶叶');
insert into store (store_no,store_name,store_intro) values ('2018100047','郑茗村旗舰店','茶叶');
insert into store (store_no,store_name,store_intro) values ('2018100048','清轩茶叶店','茶叶');
insert into store (store_no,store_name,store_intro) values ('2018100049','天宇奶茶',null);
insert into store (store_no,store_name,store_intro) values ('2018100050','gurado果然豆旗舰店',null);
insert into store (store_no,store_name,store_intro) values ('2018100051','香飘飘奶香店',null);
insert into store (store_no,store_name,store_intro) values ('2018100052','寻茶记1867',null);
insert into store (store_no,store_name,store_intro) values ('2018100053','西贝餐饮',null);
insert into store (store_no,store_name,store_intro) values ('2018100054','辣么棒',null);
insert into store (store_no,store_name,store_intro) values ('2018100055','H2','西餐');
insert into store (store_no,store_name,store_intro) values ('2018100056','义面屋',null);
insert into store (store_no,store_name,store_intro) values ('2018100057','91加盟儿童玩具店',null);

delete from type_product_map;
delete from product_param;
delete from product;
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0001100401','华硕鼠标',45800,'0','0001'); 
insert into type_product_map (type_id,product_code) values ('040207','0001100401');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100402','弘基鼠标',60000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040207','0002100402');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100403','联想笔记本',6500000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040211','0006100403');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100404','戴尔笔记本',5500000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040211','0006100404');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100406','儿童益智恐龙模型',145000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('0303','0008100406');
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100406','适用年龄','5-7岁',1); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100406','产品材质','塑料/胶制',2); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100406','型号','幻影忍者',3); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100406','颜色','黄',4);

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100407','乐高积木巨龙战车',68000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('030102','0008100407');
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100407','适用年龄','3-4岁',1); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100407','产品材质','木质',2); 
insert into product_param (product_code,param_name,param_value,show_order)values ('0008100407','颜色','蓝、绿',4);

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100405','火银狐V6',50000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040207','0006100405');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100406','罗技 LINE FRIENDS',145000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040207','0006100406');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100407','小米 便携鼠标',145000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040207','0006100407');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100408','SILVER LINK 蓝牙',169000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040207','0006100408');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0001100402','华硕（ASUS）游戏笔记本电脑',7199000,'0','0001'); 
insert into type_product_map (type_id,product_code) values ('040211','0001100402');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0001100403','华硕（ASUS）飞行堡垒五代笔记本手提电脑',6999000,'0','0001'); 
insert into type_product_map (type_id,product_code) values ('040211','0001100403');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0001100404','华硕(ASUS)顽石YX560UD游戏本',4999000,'0','0001'); 
insert into type_product_map (type_id,product_code) values ('040211','0001100404');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100403','Acer/宏碁 炫6 A615 笔记本电脑',3999000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100403');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100404','Acer宏碁投影仪X118H',3099000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('0402','0002100404');
insert into type_product_map (type_id,product_code) values ('09','0002100404');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100405','DLP主动快门式3D眼镜',69000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('0402','0002100405');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100406','暗影骑士3枪神版游戏本',6999000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100406');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100407','翼5 A515-51G',8199000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100407');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100408','TMP2410商用笔记本',8199000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100408');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100409','TMP2510商用笔记本',7199000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100409');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100410','TMP238商用笔记本',6899000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('040211','0002100410');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0002100411','Veriton AP150 F3工作站',28199000,'0','0002'); 
insert into type_product_map (type_id,product_code) values ('0402','0002100411');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100501','华为(HUAWEI)平板电脑',2199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040202','0009100501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100502','苹果ipad9.7吋',6199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040202','0009100502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100503','小米(mi)平板电脑',2199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040202','0009100503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100504','华为手环',199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040210','0009100504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100505','小米手环',199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040210','0009100505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100506','苹果Watch',199000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040210','0009100506');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100507','TPB102充电棒',499000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('0402','0009100507');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100508','金士顿U盘16G',89000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040209','0009100508');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100509','金士顿U盘32G',189000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040209','0009100509');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100510','hpU盘32G',129000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040209','0009100510');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100511','hpU盘16G',69000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040209','0009100511');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100501','金士顿U盘16G',89000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040209','0006100501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006100502','金士顿U盘32G',189000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040209','0006100502');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100501','小微电动机器人',289000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('0304','0008100501');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100502','迪士尼草莓熊玩具',359000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('0302','0008100502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100503','迪士尼草莓熊双肩包',159000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('0302','0008100503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100504','愤怒的小鸟布绒玩具',29000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('0302','0008100504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100505','小太阳童车',229000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('03','0008100505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0008100506','千禧童车',219000,'0','0008'); 
insert into type_product_map (type_id,product_code) values ('03','0008100506');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100501','小微电动机器人',289000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('0304','0057100501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100502','迪士尼草莓熊玩具',359000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('0302','0057100502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100503','迪士尼草莓熊双肩包',159000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('0302','0057100503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100504','愤怒的小鸟布绒玩具',29000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('0302','0057100504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100505','小太阳童车',229000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('03','0057100505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100506','千禧童车',219000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('03','0057100506');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100405','DLP主动快门式3D眼镜',69000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('0402','0010100405');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100406','暗影骑士3枪神版游戏本',6999000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('040211','0010100406');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100407','翼5 A515-51G',8199000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('040211','0010100407');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100408','TMP2410商用笔记本',8199000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('040211','0010100408');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100409','TMP2510商用笔记本',7199000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('040211','0010100409');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100410','TMP238商用笔记本',6899000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('040211','0010100410');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0010100411','Veriton AP150 F3工作站',28199000,'0','2018100010'); 
insert into type_product_map (type_id,product_code) values ('0402','0010100411');


insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100501','华为(HUAWEI)平板电脑',2199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100502','苹果ipad9.7吋',6199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100503','小米(mi)平板电脑',2199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100504','华为手环',199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040210','0022100504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100505','小米手环',199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040210','0022100505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100506','苹果Watch',199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040210','0022100506');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100507','TPB102充电棒',499000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('0402','0022100507');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100508','金士顿U盘16G',89000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040209','0022100508');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100509','金士顿U盘32G',189000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040209','0022100509');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100510','hpU盘32G',129000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040209','0022100510');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100511','hpU盘16G',69000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040209','0022100511');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0057100507','极乐贝贝厨房套装',169000,'0','2018100057'); 
insert into type_product_map (type_id,product_code) values ('03','0057100507');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100501','小米6',1699000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100502','小米7',2999000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100503','小米8',3999000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100504','红米3',2899000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100505','红米2',2899000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100506','红米note2',2799000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100506');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100507','红米note2增强版',3799000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040201','0021100507');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100508','小米手环',199000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040210','0021100508');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100509','小米(mi)平板电脑3',2199000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040202','0021100509');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100510','小米(mi)平板电脑4',2999000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040202','0021100510');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0021100511','小米(mi)平板电脑4 plus',3799000,'0','2018100021'); 
insert into type_product_map (type_id,product_code) values ('040202','0021100511');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100512','小米6',1699000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100512');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100513','小米7',2999000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100513');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100514','小米8',3999000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100514');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100515','红米3',2899000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100515');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100516','红米2',2899000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100516');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100517','红米note2',2799000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100517');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100518','红米note2增强版',3799000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100518');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100519','小米(mi)平板电脑4',2999000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040202','0009100519');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100520','小米(mi)平板电脑4 plus',3799000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040202','0009100520');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100512','小米6',1699000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100512');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100513','小米7',2999000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100513');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100514','小米8',3999000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100514');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100515','红米3',2899000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100515');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100516','红米2',2899000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100516');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100517','红米note2',2799000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100517');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100518','红米note2增强版',3799000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100518');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100519','小米(mi)平板电脑3',2199000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100519');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100520','小米(mi)平板电脑4',2999000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100520');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100521','小米(mi)平板电脑4 plus',3799000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040202','0022100521');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100521','荣耀6',699000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100521');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100522','荣耀7',999000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100522');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100523','荣耀8',1299000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100523');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100524','荣耀9',1699000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100524');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100525','荣耀10',2099000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100525');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100526','荣耀8x',1699000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040201','0009100526');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100523','荣耀6',699000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100523');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100524','荣耀7',999000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100524');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100525','荣耀8',1299000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100525');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100526','荣耀9',1699000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100526');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100527','荣耀10',2099000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100527');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0022100528','荣耀8x',1699000,'0','2018100022'); 
insert into type_product_map (type_id,product_code) values ('040201','0022100528');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100527','小霸王掌机PSP',299000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100527');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100528','彦博游戏手柄',299000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100528');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100529','索尼游戏手柄',289000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100529');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100530','索尼游戏专用键盘',1699000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100530');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100531','索尼PS',8899000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100531');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0009100532','罗技飞行模拟器',1799000,'0','2018100009'); 
insert into type_product_map (type_id,product_code) values ('040205','0009100532');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101501','小霸王掌机PSP',299000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101502','彦博游戏手柄',299000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101503','索尼游戏手柄',289000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101503');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101504','索尼游戏专用键盘',1699000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101504');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101505','索尼PS',8899000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101505');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0006101506','罗技飞行模拟器',1799000,'0','0006'); 
insert into type_product_map (type_id,product_code) values ('040205','0006101506');

insert into product (product_code,product_name,product_price,product_status,store_no)values ('0023101501','索尼游戏手柄',289000,'0','2018100023'); 
insert into type_product_map (type_id,product_code) values ('040205','0023101501');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0023101502','索尼游戏专用键盘',1699000,'0','2018100023'); 
insert into type_product_map (type_id,product_code) values ('040205','0023101502');
insert into product (product_code,product_name,product_price,product_status,store_no)values ('0023101503','索尼PS',8899000,'0','2018100023'); 
insert into type_product_map (type_id,product_code) values ('040205','0023101503');
