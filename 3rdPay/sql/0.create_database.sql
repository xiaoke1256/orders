CREATE USER 'thirdpayUser'@'%' IDENTIFIED BY 'xiaoke_1256';

create schema thirdpay default character set utf8 collate utf8_general_ci;

GRANT ALL ON thirdpay.* TO 'thirdpayUser'@'%';