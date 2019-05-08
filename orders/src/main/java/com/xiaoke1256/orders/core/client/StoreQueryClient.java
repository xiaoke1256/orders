package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=StoreQueryFallbackFactory.class,path="product")
public interface StoreQueryClient extends StoreQueryService {

	@Override
	@RequestMapping(value="/store/queryAvailable",method=RequestMethod.GET)
	public List<Store> queryAvailableStore();

	@Override
	@RequestMapping(value="/store/{storeNo}",method=RequestMethod.GET)
	public Store getStore(@PathVariable("storeNo") String storeNo) ;

}
