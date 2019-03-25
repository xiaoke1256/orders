package com.xiaoke1256.orders.core.client;

import org.springframework.cloud.netflix.feign.FeignClient;

import com.xiaoke1256.orders.product.api.SecKillSupportService;
import com.xiaoke1256.orders.product.fallback.SecKillSupportFallbackFactory;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=SecKillSupportFallbackFactory.class,path="product")
public interface SecKillSupportClient extends SecKillSupportService {

}
