package com.xiaoke_1256.orders.bigdata.aimode.dto;

import com.xiaoke_1256.orders.bigdata.common.ml.dto.PredictResult;
import com.xiaoke_1256.orders.bigdata.product.dto.SimpleProductStatic;

import java.util.List;
import java.util.Map;

/**
 * 聚集结果
 */
public class ProductClusterResult {
    private List<PredictResult<SimpleProductStatic>> samplesWithLabel;

    private Map<Integer,String> labelMap;

    Long modelId;

    public List<PredictResult<SimpleProductStatic>> getSamplesWithLabel() {
        return samplesWithLabel;
    }

    public void setSamplesWithLabel(List<PredictResult<SimpleProductStatic>> samplesWithLabel) {
        this.samplesWithLabel = samplesWithLabel;
    }

    public Map<Integer, String> getLabelMap() {
        return labelMap;
    }

    public void setLabelMap(Map<Integer, String> labelMap) {
        this.labelMap = labelMap;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }
}
