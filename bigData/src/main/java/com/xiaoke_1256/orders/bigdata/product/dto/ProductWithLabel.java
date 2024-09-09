package com.xiaoke_1256.orders.bigdata.product.dto;

import com.xiaoke_1256.orders.bigdata.product.model.Product;

import java.io.Serializable;

public class ProductWithLabel extends Product implements Serializable {
    private int orderCount;
    private String label;

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
