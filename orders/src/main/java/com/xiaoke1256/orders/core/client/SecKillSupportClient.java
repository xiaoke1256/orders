package com.xiaoke1256.orders.core.client;

import com.xiaoke1256.orders.product.api.SecKillSupportService;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="api-product",
        url="${remote.api.product.uri}",
        fallbackFactory=SecKillSupportFallbackFactory.class,
        contextId="SecKillSupport",
        path="product")
public interface SecKillSupportClient extends SecKillSupportService {

}
