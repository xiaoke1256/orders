package com.xiaoke1256.orders.search.dao;

import java.sql.Timestamp;
import java.util.List;

import com.xiaoke1256.orders.search.bo.Product;


public interface ProductDao {
	public Product getProductByCode(String productCode);
	
	public List<Product> queryModifed(Timestamp lastTime,String productStatus);
}
