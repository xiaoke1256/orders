package com.xiaoke1256.orders.search.dao;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.search.bo.Product;

@Repository
public interface ProductDao {
	public Product getProductByCode(String productCode);
}
