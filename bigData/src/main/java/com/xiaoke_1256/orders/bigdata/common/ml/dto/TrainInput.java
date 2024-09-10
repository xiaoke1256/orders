package com.xiaoke_1256.orders.bigdata.common.ml.dto;

import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;

/**
 * 训练输入参数
 */
public class TrainInput {
    /**
     * 样本查询条件
     */
    private ProductCondition condition;
    /**
     * 分类数
     */
    private int numClusters;
    /**
     * 迭代次数
     */
    private int numIterator;

    /**
     * 分类方案（即联外一个模型的执行结果）
     */
    private Long clusterFormulaModelId;

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

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public int getNumIterator() {
        return numIterator;
    }

    public void setNumIterator(int numIterator) {
        this.numIterator = numIterator;
    }

    public Long getClusterFormulaModelId() {
        return clusterFormulaModelId;
    }

    public void setClusterFormulaModelId(Long clusterFormulaModelId) {
        this.clusterFormulaModelId = clusterFormulaModelId;
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
