package com.xiaoke1256.orders.product.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.product.bo.ProductParam;
import com.xiaoke1256.orders.product.dao.ProductParamDao;


@Repository
public class ProductParamDaoImpl extends BaseDaoImpl implements ProductParamDao {

	@Override
	public ProductParam getById(Long paramId) {
		ProductParam param = this.getSqlSession()
			.selectOne("com.xiaoke1256.orders.product.dao.ProductParamDao.getById", paramId);
		return param;
	}

	@Override
	public List<ProductParam> getByProductCode(String productCode) {
		List<ProductParam> params = this.getSqlSession()
				.selectList("com.xiaoke1256.orders.product.dao.ProductParamDao.getByProductCode", productCode);
		return params;
	}

}
