package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.Product;
import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import org.springframework.beans.BeanUtils;

public class ProductTypeAssembler {
    public static ProductType toDomain(ProductTypeEntity productTypeEntity){
        ProductType domain = new ProductType();
        BeanUtils.copyProperties(productTypeEntity,domain);
        return domain;
    }
}
