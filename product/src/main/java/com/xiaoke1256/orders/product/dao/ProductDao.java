package com.xiaoke1256.orders.product.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;

public interface ProductDao {
	public Product getProductByCode(String productCode);
	
	public SimpleProduct getSimpleProductByCode(String productCode);
	
	public List<Product> queryModifed(@Param("lastTime")Timestamp lastTime,@Param("productStatus")String productStatus);
	
	public List<Product> queryByCondition(ProductCondition condition);
	
	public Integer countByCondition(ProductCondition condition);
	
	public void updateSecKill(String productCode,String isSecKill);
}
