package com.xiaoke1256.orders.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xiaoke1256.orders.product.bo.ProductParam;

/**
 * @deprecated
 */
@Deprecated
public interface ProductParamDao {
	
	public ProductParam getById(@Param("id")Long paramId);
	
	public List<ProductParam> getByProductCode(@Param("productCode")String productCode);

}
