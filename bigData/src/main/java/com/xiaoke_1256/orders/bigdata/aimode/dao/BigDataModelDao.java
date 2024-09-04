package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataModelMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModel;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class BigDataModelDao {

    @Autowired
    private BigDataModelMapper bigDataModelMapper;

    public void save(BigDataModelWithBLOBs bigDataModel){
        bigDataModel.setCreateTime(new Date());
        bigDataModelMapper.insert(bigDataModel);
    }

}
