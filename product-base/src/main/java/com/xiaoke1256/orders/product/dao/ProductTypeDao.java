package com.xiaoke1256.orders.product.dao;

import java.util.List;

import com.xiaoke1256.orders.product.bo.ProductType;

public interface ProductTypeDao {
	public List<ProductType> getTypesByProductCode(String productCode);
}
