orders
=

## 一、概述

**一个简单的电子商务平台，探讨了以下情况：**
1. 高并发环境下订单的受理。
2. 商品搜索(ES)。
3. 高吞吐量的订单流转。  

**This is a simple e-commerce platform.We discuss these topics:**
1. Receiving orders in high concurrency environment.
2. Searching porducts(ES).
3. Order circulation in high throughput capacity.

## 二、流程图

### 购物流程
```
                ┌----------┐   ┌-> 开启物流流程 -> 签收
                |          V   |
  下订单 -> 支付 ┴> 确认订单 -> 出库  ┬> (确认收货) -┬> 清结算 -> 打款 -> 交易成功(结束) 
     |       |       |         |    |             |
  (取消)    (取消) (取消)     (取消) └->(时限到期)--┘
     └---┬---┘       └---┬-----┘
         |               |
         |               V 
         |              退款
         |               |
         |               V
         └--------->交易取消(结束)
```

### 退货流程
```
                                    ┌-> 开启物流流程 -> 卖家签收
                                    |
买家发起退货 -> 卖家确认 -(同意)-> 买家发货 ┬-> (确认收货)-┬->打款给买家->对卖家进行清结算->退货成功(结束) 
                   |                ↑    └-> (时限到期)-┘      
	     (拒绝或超时)            |                                                   
                   |                |       
                   V                |
               平台方仲裁            |
                   ├----(同意退货)---┘
                   |
              (不同意退货)
                   |
                   V
             退货取消(结束)
	     
	     
                   ┌-> 开启物流流程 -> 卖家签收
                   |
卖家发起召回 -> 买家发货 ┬-> (确认收货)-┬->打款给买家->对卖家进行清结算->召回成功(结束) 
                   |   └-> (时限到期)-┘
	     (拒绝或超时)            
                   |                
                   V                
             召回取消(结束)
  ```

## 三、如何运行本项目

### 1、安装数据库

先安装一个MySql实例。

以root用户登录Mysql,在源代码中找到并执行以下脚本，创建orders数据库：

* */orders/sql/0.create_database.sql*

以ordersUser用户登录，依次执行以下脚本，创建数据表：

* */orders/sql/1.create_tables.sql*
* */orders/sql/2.re_create_index.sql*
* */orders/sql/3.collect_static.sql*
* */orders/sql/4.add_status_4_orders.sql*
* */orders/sql/5.create_payment_table.sql*
* */orders/sql/6.add_insert_time_4_sub_order.sql*
* */orders/sql/7.create_logistics_table.sql*
* */orders/sql/8.create_settle_tables.sql*

切换回root用户，执行以下脚本，创建product数据库：

* */product/sql/create_database.sql*

用productUser用户登录MySql，再执依次行以下脚本，创建数据表并初始化数据：

* */product/sql/create_tables.sql*
* */product/sql/init_product_type.sql*
* */product/sql/init_test_data.sql*
* */product/sql/20180711_add_in_sec_kill.sql*
* */product/sql/20190508_add_col_4_store.sql*
* */product/sql/20190510_init_store_data.sql*

切换回root用户登录MySql，执行以下脚本，创建thirdpay数据库：

* */3rdPay/sql/0.create_database.sql*

切换成thirdpayUser用户，执行以下脚本创建表：

* */3rdPay/sql/2.create_third_pay_order_table.sql*

### 2、安装Elasticsearch

安装Elasticsearch-5.6.9。

安装ik中文分词器（5.6.9版本）。

安装elasticsearch-head-5.0.0 。

安装postman。

利用 elasticsearch-head 创建索引“prod”；然后利用 postman 执行以下命令：

```
PUT  http://localhost:9200/prod
{
  "mappings": {
    "product": {
    	"properties":{
	       "code":{"type":"keyword"},
	       "name":{"type":"text","index":"true","analyzer":"ik_max_word","search_analyzer":"ik_max_word"},
	       "intro":{"type":"text","index":"true","analyzer":"ik_max_word","search_analyzer":"ik_max_word"},
	       "price":{"type":"double"},
	       "store_no":{"type":"keyword"},
	       "store_name":{"type":"text","index":"true","analyzer":"ik_max_word","search_analyzer":"ik_max_word"},
	       "upd_time":{"type": "date","format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"},
	       "type_id":{"type":"keyword"},
		   "type_name":{"type":"text","analyzer":"ik_max_word","search_analyzer":"ik_max_word"},
		   "params":{
		   		"type":"nested",
		   		"properties":{
		   			"param_name":{"type":"keyword"},
		   			"param_value":{"type":"text","analyzer":"ik_max_word","search_analyzer":"ik_max_word"}
		   		}
		   }
	    }
    },
    "product_user_map": {
      "properties":{
	     "user_id":{"type":"keyword"},
	     "score":{"type":"double"},
	     "upd_time":{"type": "date","format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"}
	  },
      "_parent": {
        "type": "product"
      }
    }
  }
}
```

### 3、安装redis

安装个redis实例即可。

### 4、安装zookeeper

安装一个zookeeper实例。启动实例后，运行 zkCli.sh ，键入以下命令：

```
create /zookeeper/3rdpay 3rdpay
create /zookeeper/3rdpay/expired_order expired_order

create /zookeeper/orders orders
create /zookeeper/orders/sending_expired sending_expired

create /zookeeper/orders/settle settle

create /zookeeper/orders/make_money make_money
```

### 5、配置各个服务器的域名

修改/etc/hosts文件，添加以下几行:
```
# zookeeper所在机器的IP地址
192.168.xx.100  zk1

# discovery-eureka.war服务所部属的服务器IP地址
192.168.xx.121  peer1
192.168.xx.122  peer2

# 3rdpay.war服务所部属的服务器IP地址
192.168.xx.121	3rdpay
192.168.xx.122	3rdpay2
	
# 安装ElasticSearch 机器的IP地址
192.168.xx.100	p.es
```

### 6、启动应用
