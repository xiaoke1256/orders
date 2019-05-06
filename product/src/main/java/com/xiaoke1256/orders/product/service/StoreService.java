package com.xiaoke1256.orders.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.product.bo.Store;
import com.xiaoke1256.orders.product.dao.StoreDao;

@Service
@Transactional
public class StoreService {
	
	private StoreDao storeDao;
	
	@Transactional(readOnly=true)
	public List<Store> queryAvailableStores() {
		return storeDao.queryAllStores();
		
	}
}
