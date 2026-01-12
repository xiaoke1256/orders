```shell
# mysql数据库
kubectl apply -f local-pv.yml
kubectl apply -f  mysql-db.yml

# filebeat 配置
kubectl apply -f filebeat-conf.yaml

# 商品微服务
kubectl apply -f product.yml
```
