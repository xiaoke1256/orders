package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataClusterDefinedMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefined;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BigDataClusterDefinedDao {
    @Autowired
    private BigDataClusterDefinedMapper bigDataClusterDefinedMapper;

    public void save(BigDataClusterDefined clusterDefined){
        bigDataClusterDefinedMapper.insertSelective(clusterDefined);
    }
}
