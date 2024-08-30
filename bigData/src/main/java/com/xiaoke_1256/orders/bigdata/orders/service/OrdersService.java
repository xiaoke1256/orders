package com.xiaoke_1256.orders.bigdata.orders.service;

import com.xiaoke_1256.orders.bigdata.orders.dao.OrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersService {
    @Autowired
    private OrderItemDao orderItemDao;
    /**
     * 按productCode count
     */
    public int countByProductCode(String productCode){
        return orderItemDao.countByProductCode(productCode);
    }
}
