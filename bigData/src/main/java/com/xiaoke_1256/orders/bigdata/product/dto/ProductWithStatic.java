package com.xiaoke_1256.orders.bigdata.product.dto;

import com.xiaoke_1256.orders.bigdata.product.model.Product;

import java.io.Serializable;

/**
 * 加上统计信息的商品
 */
public class ProductWithStatic extends Product implements Serializable {
    private int orderCount;

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
