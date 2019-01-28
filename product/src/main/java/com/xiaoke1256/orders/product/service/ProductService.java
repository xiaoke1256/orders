package com.xiaoke1256.orders.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dao.ProductDao;
import com.xiaoke1256.orders.product.vo.ProductCondition;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductDao productDao;
	
	@Transactional(readOnly=true)
	public QueryResult<Product> searchProductByCondition(ProductCondition condition){
		return null;//productDao.queryByCondition(condition);
	}
	
	@Transactional(readOnly=true)
	public Product getProductByCode(String productCode) {
		return productDao.getProductByCode(productCode);
	}
}
