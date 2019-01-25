package com.xiaoke1256.orders.product.dao.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dao.ProductDao;


@Repository
public class ProductDaoImpl extends BaseDaoImpl implements ProductDao  {

	@Override
	public Product getProductByCode(String productCode) {
		Product p = this.sqlSessionTemplate
				.selectOne("com.xiaoke1256.orders.product.dao.ProductDao.getProductByCode", productCode);
		return p;
	}

	@Override
	public List<Product> queryModifed(Timestamp lastTime, String productStatus) {
		Map<String, Object> params = new HashMap<>();
		params.put("lastTime", lastTime);
		params.put("productStatus", productStatus);
		List<Product> list = this.sqlSessionTemplate
				.selectList("com.xiaoke1256.orders.product.dao.ProductDao.queryModifed", params);
		return list;
	}

}
