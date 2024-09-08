package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataClusterObjectMapMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterObjectMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BigDataClusterObjectMapDao {
    @Autowired
    private BigDataClusterObjectMapMapper bigDataClusterObjectMapMapper;

    public void save(BigDataClusterObjectMap clusterObjectMap){
        bigDataClusterObjectMapMapper.insert(clusterObjectMap);
    }
}
