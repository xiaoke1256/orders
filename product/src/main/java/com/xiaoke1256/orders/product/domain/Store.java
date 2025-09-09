package com.xiaoke1256.orders.product.domain;

import java.util.Date;
import java.sql.Timestamp;

public class Store {

    private static final long serialVersionUID = 1L;
    private String storeNo;
    private String storeName;
    private String storeIntro;
    private String payType;
    private String payAccountNo;
    private Date insertTime;
    private Date updateTime;
    public String getStoreNo() {
        return storeNo;
    }
    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }
    public String getStoreName() {
        return storeName;
    }
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getStoreIntro() {
        return storeIntro;
    }
    public void setStoreIntro(String storeIntro) {
        this.storeIntro = storeIntro;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getPayAccountNo() {
        return payAccountNo;
    }
    public void setPayAccountNo(String payAccountNo) {
        this.payAccountNo = payAccountNo;
    }
    public Date getInsertTime() {
        return insertTime;
    }
    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
