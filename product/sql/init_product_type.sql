--大类：服装、箱包、玩具、家电、化妆品、运动、游戏、书、办公、美食
delete from product_type;
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('01','服装',null,null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0101','男装','01',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0102','女装','01',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0103','内衣','01',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0104','鞋袜','01',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('02','箱包',null,null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0201','钱包','02',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0202','旅行箱','02',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0203','双肩包','02',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0204','公文包','02',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0205','手提包','02',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('03','玩具',null,null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0301','积木','03',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030101','拼插','0301',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030102','建构','0301',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030103','磁力','0301',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0302','布偶','03',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0303','模型','03',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030301','船模','0303',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030302','航空模型','0303',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('030303','摆件','0303',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0304','电玩','03',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04','电子',null,null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0401','家电','04',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040101','电视','0401',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040102','dvd','0401',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040103','洗衣机','0401',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040104','电冰箱','0401',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040105','扫地机','0401',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040108','吸尘器','0401',null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040106','厨电','0401',null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010601','电饭煲','040106',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010602','微波炉','040106',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('04010603','洗碗机','040106',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040109','取暖机','0401',null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('0402','数码','04',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040201','手机','0402',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040202','平板电脑','0402',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040211','笔记本','0402',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040203','台式机','0402',null,3);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040204','相机/摄像','0402',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040205','游戏设备','0402',null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040206','影音','0402',null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040207','鼠标','0402',null,7);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040208','键盘','0402',null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040209','U盘','0402',null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('040210','手环','0402',null,10);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('05','化妆品',null,null,5);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('06','运动',null,null,6);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('07','游戏',null,null,7);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('08','书',null,null,8);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('09','办公',null,null,9);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('10','美食',null,null,10);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1001','零食','10',null,1);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1002','茶饮','10',null,2);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1004','餐饮','10',null,4);
insert into product_type (type_id,type_name,parent_type_id,type_desc,show_order)
	values ('1003','土特产','10',null,3);