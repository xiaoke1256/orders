package com.xiaoke_1256.orders.bigdata.aimode.dao;

import com.xiaoke1256.orders.common.page.BaseCondition;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dto.ModelSearchCondition;
import com.xiaoke_1256.orders.bigdata.aimode.mapper.BigDataModelMapper;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModel;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelExample;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelKey;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class BigDataModelDao {

    @Autowired
    private BigDataModelMapper bigDataModelMapper;

    public void save(BigDataModelWithBLOBs bigDataModel){
        bigDataModel.setCreateTime(new Date());
        bigDataModelMapper.insert(bigDataModel);
    }

    public BigDataModelWithBLOBs getDetail(Long modelId){
        BigDataModelKey key = new BigDataModelKey();
        key.setModelId(modelId);
        return bigDataModelMapper.selectByPrimaryKey(key);
    }

    public QueryResult<BigDataModel> searchModel(ModelSearchCondition condition){
        BigDataModelExample example = new BigDataModelExample();
        example.setOrderByClause("model_id desc");
        long total = bigDataModelMapper.countByExample(example);
        bigDataModelMapper.selectByExample(example);
        int offset = (condition.getPageNo()-1)*condition.getPageSize();
        int limit = condition.getPageSize();
        RowBounds rowRounds = new RowBounds(offset,limit);
        List<BigDataModel> retList = bigDataModelMapper.selectByExampleWithRowbounds(example, rowRounds);
        return new QueryResult<BigDataModel>(condition.getPageNo() , condition.getPageSize(), (int)total, retList);
    }

    public List<BigDataModel> findAll(){
        BigDataModelExample example = new BigDataModelExample();
        return bigDataModelMapper.selectByExample(example);
    }

}
