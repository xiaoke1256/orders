
备份命令
```shell
mysqldump -u root -pxiaoke_1256 --databases orders product thirdpay > /root/db-dump/orders.sql 
```

恢复命令
```shell
mysql -u root -pxiaoke_1256 < /root/db-dump/orders.sql
```
