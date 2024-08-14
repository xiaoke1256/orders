CREATE USER 'ordersUser'@'%' IDENTIFIED BY 'xiaoke_1256';

create schema orders default character set utf8 collate utf8_general_ci;

GRANT ALL ON orders.* TO 'ordersUser'@'%';

-- 主从复制配置

-- # my.cnf 需要加如下配置：
-- #log-bin=mysql-bin
-- # #主从库此值不能相等 从库 server-id=2
-- #server-id=1

-- 主库
CREATE USER 'slaveUser'@'127.0.0.1' IDENTIFIED BY 'xiaoke_1256';
GRANT REPLICATION SLAVE ON *.* TO 'slaveUser'@'127.0.0.1';
FLUSH PRIVILEGES;
SHOW MASTER STATUS;

ALTER USER 'slaveUser'@'127.0.0.1' IDENTIFIED WITH mysql_native_password by 'xiaoke_1256';

-- 主库加锁
FLUSH TABLES WITH READ LOCK;

-- # 在主库操作系统中执行：
-- mysqldump -uroot -pPassw0rd@_ --all-databases > /opt/all-20231226.sql
-- # /opt/all-20231226.sql 复制到从库机器上
-- # 在从库的操作系统中执行：
-- mysql -uroot -pPassw0rd@_ < /opt/all-20231226.sql
-- 执行
UNLOCK TABLES; -- 解锁

-- # 登上从库执行
CHANGE MASTER TO MASTER_HOST='localhost', MASTER_PORT=3306,
    MASTER_USER='slaveUser', MASTER_PASSWORD='xiaoke_1256',
    MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=2426; -- 其中2426 是 show master status 中显示的。

-- 需要设置为：
-- slave_sql_verify_checksum = 0

-- 如有必要设置 max_allowed_packet = 1073741824;

start slave;
show slave status ;
