package com.xiaoke1256.orders.product.bo;

import java.sql.Timestamp;

public class StoreMember {
    private String storeNo;
    private String accountNo;
    private String role;
    private String isDefaultStore;
    private Timestamp createTime;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
