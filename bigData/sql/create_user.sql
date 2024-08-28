CREATE USER 'bigDataUser'@'%' IDENTIFIED BY 'xiaoke_1256';

GRANT ALL ON orders.* TO 'bigDataUser'@'%';
GRANT ALL ON product.* TO 'bigDataUser'@'%';