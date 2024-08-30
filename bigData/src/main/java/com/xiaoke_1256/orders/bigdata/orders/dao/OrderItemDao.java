package com.xiaoke_1256.orders.bigdata.orders.dao;

import com.xiaoke_1256.orders.bigdata.orders.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemDao {
    @Autowired
    private OrderItemMapper orderItemMapper;

    public int countByProductCode(String productCode){
        return orderItemMapper.countByProductCode(productCode);
    }
}
