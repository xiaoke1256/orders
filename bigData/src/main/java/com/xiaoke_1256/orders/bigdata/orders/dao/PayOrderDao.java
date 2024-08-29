package com.xiaoke_1256.orders.bigdata.orders.dao;

import com.xiaoke_1256.orders.bigdata.orders.mapper.PayOrderMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PayOrderDao {
    private PayOrderMapper payOrderMapper;

    public long countByProductCode(){
        return 0;
    }
}
