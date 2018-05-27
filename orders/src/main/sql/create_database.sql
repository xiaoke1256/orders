CREATE USER 'ordersUser'@'%' IDENTIFIED BY 'xiaoke_1256';

create schema orders default character set utf8 collate utf8_general_ci;

GRANT ALL ON orders.* TO 'ordersUser'@'%';