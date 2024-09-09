package com.xiaoke_1256.orders.bigdata.aimode.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiaoke1256.orders.common.page.BaseCondition;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataClusterDefinedDao;
import com.xiaoke_1256.orders.bigdata.aimode.dao.BigDataModelDao;
import com.xiaoke_1256.orders.bigdata.aimode.dto.ModelSearchCondition;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataClusterDefined;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModel;
import com.xiaoke_1256.orders.bigdata.aimode.model.BigDataModelWithBLOBs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BigDataModelService {
    @Autowired
    private BigDataModelDao bigDataModelDao;

    @Autowired
    private BigDataClusterDefinedDao bigDataClusterDefinedDao;

    public void saveModel(BigDataModelWithBLOBs bigDataModel){
        bigDataModelDao.save(bigDataModel);
        String clustersJson = bigDataModel.getClassDefine();
        JSONObject clustersJsonObjects = JSON.parseObject(clustersJson);
        for(String label: clustersJsonObjects.keySet()){
            BigDataClusterDefined dataCluster = new BigDataClusterDefined();
            dataCluster.setModelId(bigDataModel.getModelId());
            dataCluster.setLabel(label);
            dataCluster.setLabelName(clustersJsonObjects.getString(label));
            dataCluster.setInsertTime(new Date());
            bigDataClusterDefinedDao.save(dataCluster);
        }
    }

    /**
     * 搜索模型
     * @param condition
     */
    @Transactional(readOnly = true)
    public QueryResult<BigDataModel> searchModel(ModelSearchCondition condition){
        return bigDataModelDao.searchModel(condition);
    }

    /**
     * 获取全部模型
     */
    @Transactional(readOnly = true)
    public List<BigDataModel> findAll(){
        return bigDataModelDao.findAll();
    }

    @Transactional(readOnly = true)
    public Map<String,String> getClusterDic(Long modelId){
        List<BigDataClusterDefined> clusterDefines = bigDataClusterDefinedDao.findByModelId(modelId);
        Map<String, String> dic = new LinkedHashMap<String, String>();
        for(BigDataClusterDefined clusterDefine:clusterDefines){
            dic.put(clusterDefine.getLabel(), clusterDefine.getLabelName());
        }
        return dic;
    }

}
