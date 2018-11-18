package com.xiaoke1256.orders.search.dao.impl;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.search.bo.Product;
import com.xiaoke1256.orders.search.dao.ProductDao;

@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao  {

	@Override
	public Product getProductByCode(String productCode) {
		Product p = this.sqlSessionTemplate
				.selectOne("com.xiaoke1256.orders.search.dao.ProductDao.getProductByCode", productCode);
		return p;
	}

}
