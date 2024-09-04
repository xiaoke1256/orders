package com.xiaoke_1256.orders.bigdata.aimode.service;

import com.xiaoke1256.orders.common.page.BaseCondition;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataModelDao;
import com.xiaoke_1256.orders.bigdata.aimode.dto.ModelSearchCondition;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModel;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BigDataModelService {
    @Autowired
    private BigDataModelDao bigDataModelDao;

    public void saveModel(BigDataModelWithBLOBs bigDataModel){
        bigDataModelDao.save(bigDataModel);
    }

    /**
     * 搜索模型
     * @param condition
     */
    @Transactional(readOnly = true)
    public QueryResult<BigDataModel> searchModel(ModelSearchCondition condition){
        return bigDataModelDao.searchModel(condition);
    }
}
