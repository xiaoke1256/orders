package com.xiaoke_1256.orders.bigdata.product.dto;

/**
 * 预测的输入
 */
public class ProductPredictInput {
    private ProductCondition condition;
    /**
     * 模型地址
     */
    private String modelPath;

    /**
     * 模型主键
     */
    private Long modelId;

    /**
     * 系数
     */
    private double orderCountCoefficient;
    private double productPriceCoefficient;

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

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public double getOrderCountCoefficient() {
        return orderCountCoefficient;
    }

    public void setOrderCountCoefficient(double orderCountCoefficient) {
        this.orderCountCoefficient = orderCountCoefficient;
    }

    public double getProductPriceCoefficient() {
        return productPriceCoefficient;
    }

    public void setProductPriceCoefficient(double productPriceCoefficient) {
        this.productPriceCoefficient = productPriceCoefficient;
    }
}
