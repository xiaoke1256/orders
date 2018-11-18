package com.xiaoke1256.orders.search.dao;

import com.xiaoke1256.orders.search.bo.Product;


public interface ProductDao {
	public Product getProductByCode(String productCode);
}
