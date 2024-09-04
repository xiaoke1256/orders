CREATE USER 'bigDataUser'@'%' IDENTIFIED BY 'xiaoke_1256';

GRANT ALL ON orders.* TO 'bigDataUser'@'%';
GRANT ALL ON product.* TO 'bigDataUser'@'%';

create schema big_data default character set utf8mb4 collate utf8mb4_bin;
GRANT ALL ON big_data.* TO 'bigDataUser'@'%';

