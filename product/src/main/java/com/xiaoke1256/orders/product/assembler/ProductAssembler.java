package com.xiaoke1256.orders.product.assembler;

import com.xiaoke1256.orders.product.domain.Product;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import org.springframework.beans.BeanUtils;

public class ProductAssembler {
    public static Product toDomain(ProductEntity productEntity){
        if(productEntity==null){
            return null;
        }
        Product domain = new Product();
        BeanUtils.copyProperties(productEntity,domain);
        return domain;
    }

    public static com.xiaoke1256.orders.product.dto.Product toDto(ProductEntity productEntity){
        if(productEntity==null){
            return null;
        }
        com.xiaoke1256.orders.product.dto.Product dto = new com.xiaoke1256.orders.product.dto.Product();
        BeanUtils.copyProperties(productEntity,dto);
        return dto;
    }

    public static com.xiaoke1256.orders.product.bo.Product toBo(ProductEntity productEntity){
        if(productEntity==null){
            return null;
        }
        com.xiaoke1256.orders.product.bo.Product dto = new com.xiaoke1256.orders.product.bo.Product();
        BeanUtils.copyProperties(productEntity,dto);
        return dto;
    }
}
