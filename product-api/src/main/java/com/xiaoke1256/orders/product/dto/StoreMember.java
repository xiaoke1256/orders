package com.xiaoke1256.orders.product.dto;

import java.sql.Timestamp;

public class StoreMember {
    private String storeNo;
    private String accountNo;
    private String role;
    private String isDefaultStore;
    private Timestamp insertTime;
    private Timestamp updateTime;
    private Store store;

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

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
