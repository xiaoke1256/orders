package com.xiaoke1256.orders.product.domain;

import com.xiaoke1256.orders.product.dto.Store;

import java.sql.Timestamp;
import java.util.Date;

public class StoreMember {
    private String storeNo;
    private String accountNo;
    private String role;
    private String isDefaultStore;
    private Date insertTime;
    private Date updateTime;
    private com.xiaoke1256.orders.product.dto.Store store;

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIsDefaultStore() {
        return isDefaultStore;
    }

    public void setIsDefaultStore(String isDefaultStore) {
        this.isDefaultStore = isDefaultStore;
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

    public com.xiaoke1256.orders.product.dto.Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
