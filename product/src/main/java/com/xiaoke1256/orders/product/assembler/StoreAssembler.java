package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.domain.Store;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import org.springframework.beans.BeanUtils;

public class StoreAssembler {
    public static Store toDomain(StoreEntity storeEntity){
        Store domain = new Store();
        BeanUtils.copyProperties(storeEntity,domain);
        return domain;
    }
}
