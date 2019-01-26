package com.xiaoke1256.orders.product.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.orders.product.bo.Product;

public interface ProductDao {
	public Product getProductByCode(String productCode);
	
	public List<Product> queryModifed(@Param("lastTime")Timestamp lastTime,@Param("productStatus")String productStatus);
}
