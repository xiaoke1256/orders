package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.Product;
import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import org.springframework.beans.BeanUtils;

import java.time.ZoneId;
import java.util.Date;

public class ProductTypeAssembler {
    public static ProductType toDomain(ProductTypeEntity productTypeEntity){
        ProductType domain = new ProductType();
        BeanUtils.copyProperties(productTypeEntity,domain);
        domain.setInsertTime(Date.from(productTypeEntity.getInsertTime().atZone(ZoneId.systemDefault()).toInstant()));
        domain.setUpdateTime(Date.from(productTypeEntity.getUpdateTime().atZone(ZoneId.systemDefault()).toInstant()));
        return domain;
    }
}
