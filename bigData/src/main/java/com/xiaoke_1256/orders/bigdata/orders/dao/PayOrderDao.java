package com.xiaoke_1256.orders.bigdata.orders.dao;

import com.xiaoke_1256.orders.bigdata.orders.mapper.PayOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PayOrderDao {
    @Autowired
    private PayOrderMapper payOrderMapper;

    public long countByProductCode(){
        return 0;
    }
}
