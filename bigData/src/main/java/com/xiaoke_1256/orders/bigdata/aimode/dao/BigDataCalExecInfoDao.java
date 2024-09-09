package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataCalExecInfoMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfo;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataCalExecInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BigDataCalExecInfoDao {
    @Autowired
    private BigDataCalExecInfoMapper bigDataCalExecInfoMapper;

    public void save(BigDataCalExecInfo execInfo){
        bigDataCalExecInfoMapper.insert(execInfo);
    }

    public List<BigDataCalExecInfo> queryByModelId(Long modelId){
        BigDataCalExecInfoExample example = new BigDataCalExecInfoExample();
        example.createCriteria().andModelIdEqualTo(modelId);
        example.setOrderByClause("exec_id desc");
        return bigDataCalExecInfoMapper.selectByExample(example);
    }

    public void deleteByModelId(Long modelId){
        BigDataCalExecInfoExample example = new BigDataCalExecInfoExample();
        example.createCriteria().andModelIdEqualTo(modelId);
        bigDataCalExecInfoMapper.deleteByExample(example);
    }
}
