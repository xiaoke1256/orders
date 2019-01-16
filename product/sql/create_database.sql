CREATE USER 'productUser'@'%' IDENTIFIED BY 'xiaoke_1256';

create schema product default character set utf8 collate utf8_general_ci;

GRANT ALL ON product.* TO 'productUser'@'%' IDENTIFIED BY 'xiaoke_1256';