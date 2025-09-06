-- 支付方式和支付账号
alter table store add pay_type CHAR(3) null comment '支付方式，（目前全是3rdpay）';
alter table store add pay_account_no varchar(18) null comment '支付账号';