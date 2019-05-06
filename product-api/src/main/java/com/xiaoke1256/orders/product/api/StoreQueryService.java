package com.xiaoke1256.orders.product.api;

import java.util.List;

import com.xiaoke1256.orders.product.dto.Store;

/**
 * 与商铺有关的查询
 * @author Administrator
 *
 */
public interface StoreQueryService {
	
	/**
	 * 获取有用的（未关闭的）商铺
	 * */
	public List<Store> queryAvailableStore();
}
