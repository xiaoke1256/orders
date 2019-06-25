package com.xiaoke1256.orders.core.client;

import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.RespMsg;

//import org.springframework.cloud.netflix.feign.FeignClient;

import com.xiaoke1256.orders.product.api.SecKillSupportService;

//@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=SecKillSupportFallbackFactory.class,path="product")
@Component
public class SecKillSupportClient implements SecKillSupportService {

	@Override
	public RespMsg openSecKill(String productCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespMsg closeSecKill(String productCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
