package com.xiaoke1256.orders.product.assembler;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.orders.product.domain.Product;
import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import org.springframework.beans.BeanUtils;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ProductAssembler {
    public static Product toDomain(ProductEntity productEntity){
        if(productEntity==null){
            return null;
        }
        Product domain = new Product();
        BeanUtils.copyProperties(productEntity,domain);
        domain.setInsertTime(Date.from(productEntity.getInsertTime().atZone(ZoneId.systemDefault()).toInstant()));
        domain.setUpdateTime(Date.from(productEntity.getUpdateTime().atZone(ZoneId.systemDefault()).toInstant()));
        if(productEntity.getProductTypes()!=null){
            List<ProductType> typesDomain = productEntity.getProductTypes().stream().map(ProductTypeAssembler::toDomain).collect(Collectors.toList());
            domain.setProductTypes(typesDomain);
        }
        if(productEntity.getStore()!=null){
            domain.setStore(StoreAssembler.toDomain(productEntity.getStore()));
        }
//        if(productEntity.getParams()!=null){
//            List<ProductType> typesDomain = productEntity.getParams().stream().map(ProductTypeAssembler::toDomain).collect(Collectors.toList());
//            domain.setProductTypes(typesDomain);
//        }
        System.out.println("domain:"+ JSON.toJSONString(domain));
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
