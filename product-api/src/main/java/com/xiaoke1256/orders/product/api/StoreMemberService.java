package com.xiaoke1256.orders.product.api;

import com.xiaoke1256.orders.product.dto.StoreMember;

import java.util.List;

public interface StoreMemberService {
    public List<StoreMember> queryStoreMemberByAccountNo(String accountNo);
}
