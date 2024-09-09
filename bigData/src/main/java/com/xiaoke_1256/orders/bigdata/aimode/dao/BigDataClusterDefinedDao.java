package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataClusterDefinedMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefined;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefinedExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BigDataClusterDefinedDao {
    @Autowired
    private BigDataClusterDefinedMapper bigDataClusterDefinedMapper;

    public void save(BigDataClusterDefined clusterDefined){
        bigDataClusterDefinedMapper.insertSelective(clusterDefined);
    }

    public List<BigDataClusterDefined> findByModelId(Long modelId){
        BigDataClusterDefinedExample example = new BigDataClusterDefinedExample();
        example.createCriteria().andModelIdEqualTo(modelId);
        example.setOrderByClause(" label asc ");
        return bigDataClusterDefinedMapper.selectByExample(example);
    }
}
