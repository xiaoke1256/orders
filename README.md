# orders
**一个简单的电子商务平台，探讨了以下情况：**
1. 高并发环境下订单的受理。
2. 商品搜索(ES)。
3. 高吞吐量的订单流转。  

**This is a simple e-commerce platform.We discuss these topics:**
1. Receiving orders in high concurrency environment.
2. Searching porducts(ES).
3. Order circulation in high throughput capacity.

### 购物流程
```
                          ┌-> 开启物流流程 -> 签收
                          | 
  下订单 -> 支付 -> 备货 -> 出库  ┬> (确认收货) -┬> 清结算 -> 打款 -> 交易成功(结束) 
     |       |       |       |   |             |
  (取消)    (取消)  (取消）   |   └->(时限到期)--┘
     └---┬---┘       |     (取消) 
         |           └---┬---┘
         |               V 
         |              退款
         |               |
         |               V
         └--------->交易取消(结束)
```
