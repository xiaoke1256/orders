package com.xiaoke_1256.orders.bigdata.aimode.dto;

import java.util.Date;
import java.util.Map;

public class BigDataModelDto {
    private Long modelId;

    private String modelPath;

    private String modelName;

    private String modelDesc;

    private String modelType;

    /**
     * 算法
     */
    private String algorithmType;

    /**
     * 训练参数
     */
    private TrainParam trainParam;

    /**
     * 分类定义
     */
    private Map<String,String> classDefine;

    private Date createTime;

    private String createBy;

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelDesc() {
        return modelDesc;
    }

    public void setModelDesc(String modelDesc) {
        this.modelDesc = modelDesc;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public TrainParam getTrainParam() {
        return trainParam;
    }

    public void setTrainParam(TrainParam trainParam) {
        this.trainParam = trainParam;
    }

    public Map<String, String> getClassDefine() {
        return classDefine;
    }

    public void setClassDefine(Map<String, String> classDefine) {
        this.classDefine = classDefine;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public static class TrainParam{
        private double productPriceCoefficient;
        private double orderCountCoefficient;
        private int numClusters;
        private int numIterator;

        public double getProductPriceCoefficient() {
            return productPriceCoefficient;
        }

        public void setProductPriceCoefficient(double productPriceCoefficient) {
            this.productPriceCoefficient = productPriceCoefficient;
        }

        public double getOrderCountCoefficient() {
            return orderCountCoefficient;
        }

        public void setOrderCountCoefficient(double orderCountCoefficient) {
            this.orderCountCoefficient = orderCountCoefficient;
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
    }
}
