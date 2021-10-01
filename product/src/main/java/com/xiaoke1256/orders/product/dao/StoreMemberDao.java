package com.xiaoke1256.orders.product.dao;

import com.xiaoke1256.orders.product.bo.StoreMember;

import java.util.List;

public interface StoreMemberDao {
    public List<StoreMember> selectByAccountNo(String accountNo);

    public StoreMember getDefaultStore(String accountNo);

    public void saveStoreMember(StoreMember storeMember);
}
