package com.xiaoke_1256.orders.bigdata.product.dao;

import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.mapper.ProductMapper;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> search(ProductCondition condition){
        return productMapper.queryByCondition(condition);
    }
}
