package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataClusterObjectMapMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterObjectMap;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterObjectMapExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BigDataClusterObjectMapDao {
    @Autowired
    private BigDataClusterObjectMapMapper bigDataClusterObjectMapMapper;

    public void save(BigDataClusterObjectMap clusterObjectMap){
        bigDataClusterObjectMapMapper.insert(clusterObjectMap);
    }

    public void deleteByExecId(Long execId){
        BigDataClusterObjectMapExample example = new BigDataClusterObjectMapExample();
        example.createCriteria().andExecIdEqualTo(execId);
        bigDataClusterObjectMapMapper.deleteByExample(example);
    }

    public BigDataClusterObjectMap selectByModelIdAndObjectId(String objectType, String objectId, Long modelId ){
        return bigDataClusterObjectMapMapper.selectByModelIdAndObjectId(objectType,objectId,modelId);
    }
}
