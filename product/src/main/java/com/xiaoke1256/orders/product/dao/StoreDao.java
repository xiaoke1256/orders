package com.xiaoke1256.orders.product.dao;

import java.util.List;

import com.xiaoke1256.orders.product.bo.Store;

public interface StoreDao {
	public Store getByStoreNo(String storeNo);
	
	public List<Store> queryAllStores();

	public void saveStore(Store store);

}
