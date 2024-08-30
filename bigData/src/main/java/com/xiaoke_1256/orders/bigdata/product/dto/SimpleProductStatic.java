package com.xiaoke_1256.orders.bigdata.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 缩减字段后的商品统计信息
 */
public class SimpleProductStatic implements Serializable {
    private String productCode;
    private double productPrice;
    private int orderCount;

    public SimpleProductStatic() {
    }

    public SimpleProductStatic(String productCode, double productPrice, int orderCount) {
        this.productCode = productCode;
        this.productPrice = productPrice;
        this.orderCount = orderCount;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
