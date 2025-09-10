package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.Store;
import com.xiaoke1256.orders.product.domain.StoreMember;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import com.xiaoke1256.orders.product.entity.StoreMemberEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class StoreMemberAssembler {
    public static StoreMember toDomain(StoreMemberEntity storeMemberEntity){
        StoreMember domain = new StoreMember();
        BeanUtils.copyProperties(storeMemberEntity,domain);
        return domain;
    }
}
