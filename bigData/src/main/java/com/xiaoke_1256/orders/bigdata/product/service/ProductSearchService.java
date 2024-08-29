package com.xiaoke_1256.orders.bigdata.product.service;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke_1256.orders.bigdata.product.dao.ProductDao;
import com.xiaoke_1256.orders.bigdata.product.dto.ProductCondition;
import com.xiaoke_1256.orders.bigdata.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductSearchService {
    @Autowired
    private ProductDao productDao;

    /**
     * 按条件查询商品
     * @param productCondition
     * @return
     */
    @Transactional(readOnly = true)
    public QueryResult<Product> searchByCondition(ProductCondition productCondition){
        List<Product> list = productDao.search(productCondition);
        return new QueryResult<>( productCondition.getPageNo(), productCondition.getPageSize(), productCondition.getTotal(), list);
    }
}
