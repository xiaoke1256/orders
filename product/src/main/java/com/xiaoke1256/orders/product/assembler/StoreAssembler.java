package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.domain.Store;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import com.xiaoke1256.orders.product.entity.StoreEntity;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;

public class StoreAssembler {
    public static Store toDomain(StoreEntity storeEntity){
        Store domain = new Store();
        BeanUtils.copyProperties(storeEntity,domain);
        domain.setInsertTime(new Date(storeEntity.getInsertTime().getTime()));
        domain.setUpdateTime(new Date(storeEntity.getUpdateTime().getTime()));
        return domain;
    }

    public static StoreEntity toEntity(Store domain){
        StoreEntity storeEntity = new StoreEntity();
        BeanUtils.copyProperties(domain,storeEntity);
        if(domain.getInsertTime()!=null) {
            storeEntity.setInsertTime(new Timestamp(domain.getInsertTime().getTime()));
        }
        if(domain.getUpdateTime()!=null) {
            storeEntity.setUpdateTime(new Timestamp(domain.getUpdateTime().getTime()));
        }
        return storeEntity;
    }
}
