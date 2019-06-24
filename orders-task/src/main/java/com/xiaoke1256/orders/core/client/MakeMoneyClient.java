package com.xiaoke1256.orders.core.client;

//import org.springframework.cloud.netflix.feign.FeignClient;

import com.xiaoke1256.orders.core.api.MakeMoneyService;


//@FeignClient(name="api-orders",url="${remote.api.orders.uri}",fallbackFactory=MakeMoneyFallbackFactory.class,path="orders")
public interface MakeMoneyClient extends MakeMoneyService {
	
}