package com.xiaoke_1256.orders.bigdata.aimode.service;

import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataModelDao;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BigDataModelService {
    @Autowired
    private BigDataModelDao bigDataModelDao;

    public void saveModel(BigDataModelWithBLOBs bigDataModel){
        bigDataModelDao.save(bigDataModel);
    }
}
