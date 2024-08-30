package com.xiaoke_1256.orders.bigdata.common.ml.dto;

import java.io.Serializable;

/**
 * 预测结果
 */
public class PredictResult<T> implements Serializable {
    /**
     * 样本
     */
    private T sample;
    /**
     * 标签
     */
    private Integer label;

    public PredictResult(T sample, Integer label) {
        this.sample = sample;
        this.label = label;
    }

    public T getSample() {
        return sample;
    }

    public void setSample(T sample) {
        this.sample = sample;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }
}
