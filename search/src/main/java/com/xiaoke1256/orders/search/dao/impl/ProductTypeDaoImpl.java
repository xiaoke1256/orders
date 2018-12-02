package com.xiaoke1256.orders.search.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.search.bo.ProductType;
import com.xiaoke1256.orders.search.dao.ProductTypeDao;

@Repository
public class ProductTypeDaoImpl extends BaseDaoImpl implements ProductTypeDao {

	@Override
	public List<ProductType> getTypesByProductCode(String productCode) {
		 return this.sqlSessionTemplate
			.selectList("com.xiaoke1256.orders.search.dao.ProductTypeDao.getTypesByProductCode", productCode);
	}

}
