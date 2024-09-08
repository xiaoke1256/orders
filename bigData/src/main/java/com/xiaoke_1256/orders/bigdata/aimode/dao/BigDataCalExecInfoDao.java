package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataCalExecInfoMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BigDataCalExecInfoDao {
    @Autowired
    private BigDataCalExecInfoMapper bigDataCalExecInfoMapper;

    public void save(BigDataCalExecInfo execInfo){
        bigDataCalExecInfoMapper.insert(execInfo);
    }
}
