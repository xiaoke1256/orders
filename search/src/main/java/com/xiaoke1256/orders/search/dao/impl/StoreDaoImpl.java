package com.xiaoke1256.orders.search.dao.impl;

import com.xiaoke1256.orders.search.bo.Store;
import com.xiaoke1256.orders.search.dao.StoreDao;

public class StoreDaoImpl extends BaseDaoImpl implements StoreDao {

	@Override
	public Store getByStoreNo(String storeNo) {
		Store s = this.sqlSessionTemplate
				.selectOne("com.xiaoke1256.orders.search.dao.StoreDao.getByStoreNo", storeNo);
		return s;
	}

}
