package com.xiaoke1256.orders.product.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;

public interface ProductDao {
	Product getProductByCode(String productCode);
	
	SimpleProduct getSimpleProductByCode(String productCode);
	
	List<Product> queryModifed(@Param("lastTime")Timestamp lastTime,@Param("productStatus")String productStatus);
	
	List<Product> queryByCondition(ProductCondition condition);
	
	Integer countByCondition(ProductCondition condition);
	
	void updateSecKill(String productCode,String isSecKill);

	void updateBySelective(Product product);
}
