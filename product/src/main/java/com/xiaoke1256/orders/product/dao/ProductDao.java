package com.xiaoke1256.orders.product.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.vo.ProductCondition;

public interface ProductDao {
	public Product getProductByCode(String productCode);
	
	public List<Product> queryModifed(@Param("lastTime")Timestamp lastTime,@Param("productStatus")String productStatus);
	
	public List<Product> queryByCondition(ProductCondition condition);
	
	public Integer countByCondition(ProductCondition condition);
}
