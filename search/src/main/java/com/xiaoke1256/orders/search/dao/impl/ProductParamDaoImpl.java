package com.xiaoke1256.orders.search.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.search.bo.ProductParam;
import com.xiaoke1256.orders.search.dao.ProductParamDao;

@Repository
public class ProductParamDaoImpl extends BaseDaoImpl implements ProductParamDao {

	@Override
	public ProductParam getById(Long paramId) {
		ProductParam param = this.sqlSessionTemplate
			.selectOne("com.xiaoke1256.orders.search.dao.ProductParamDao.getById", paramId);
		return param;
	}

	@Override
	public List<ProductParam> getByProductCode(String productCode) {
		List<ProductParam> params = this.sqlSessionTemplate
				.selectList("com.xiaoke1256.orders.search.dao.ProductParamDao.getByProductCode", productCode);
		return params;
	}

}
