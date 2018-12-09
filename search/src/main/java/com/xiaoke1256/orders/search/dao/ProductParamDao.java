package com.xiaoke1256.orders.search.dao;

import java.util.List;

import com.xiaoke1256.orders.search.bo.ProductParam;

public interface ProductParamDao {
	
	public ProductParam getById(Long paramId);
	
	public List<ProductParam> getByProductCode(String productCode);

}
