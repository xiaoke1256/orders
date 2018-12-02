package com.xiaoke1256.orders.search.dao;

import java.util.List;

import com.xiaoke1256.orders.search.bo.ProductType;

public interface ProductTypeDao {
	public List<ProductType> getTypesByProductCode(String productCode);
}
