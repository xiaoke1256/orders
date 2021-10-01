package com.xiaoke1256.orders.product.api;

import java.util.List;

import com.xiaoke1256.orders.product.dto.Store;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 与商铺有关的业务
 * @deprecated  相关业务迁移到 StoreService 中去
 * @author Administrator
 *
 */
public interface StoreQueryService {
	
	/**
	 * 获取有用的（未关闭的）商铺
	 * */
	public List<Store> queryAvailableStore();

	/**
	 * 根据商铺号查询商铺
	 * @param storeNo
	 * @return
	 */
	public Store getStore(String storeNo);

}
