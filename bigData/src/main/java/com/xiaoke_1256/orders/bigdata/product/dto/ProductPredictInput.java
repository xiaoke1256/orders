package com.xiaoke_1256.orders.bigdata.product.dto;

/**
 * 预测的输入
 */
public class ProductPredictInput {
    private ProductCondition condition;
    private String modelPath;

    public ProductCondition getCondition() {
        return condition;
    }

    public void setCondition(ProductCondition condition) {
        this.condition = condition;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }
}
