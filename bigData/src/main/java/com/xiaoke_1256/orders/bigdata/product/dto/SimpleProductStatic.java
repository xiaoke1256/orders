package com.xiaoke_1256.orders.bigdata.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 缩减字段后的商品统计信息
 */
public class SimpleProductStatic implements Serializable {
    private String productCode;
    private String productName;
    private double productPrice;
    private String brand;
    private String storeNo;
    private int orderCount;
    private String label;

    public SimpleProductStatic() {
    }

    public SimpleProductStatic(String productCode,String productName, double productPrice, int orderCount) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderCount = orderCount;
    }

    public SimpleProductStatic(String label,String productCode,String productName,  String brand, String storeNo) {
        this.label = label;
        this.productCode = productCode;
        this.productName = productName;
        this.brand = brand;
        this.storeNo = storeNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

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
