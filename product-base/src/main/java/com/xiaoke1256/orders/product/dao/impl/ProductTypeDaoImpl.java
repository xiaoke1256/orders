package com.xiaoke1256.orders.product.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.product.bo.ProductType;
import com.xiaoke1256.orders.product.dao.ProductTypeDao;


@Repository
public class ProductTypeDaoImpl extends BaseDaoImpl implements ProductTypeDao {

	@Override
	public List<ProductType> getTypesByProductCode(String productCode) {
		 return this.getSqlSession()
			.selectList("com.xiaoke1256.orders.product.dao.ProductTypeDao.getTypesByProductCode", productCode);
	}

}
