package com.xiaoke1256.orders.store.intra.product.client;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.store.intra.store.client.StoreFallbackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="api-orders",url="${remote.api.orders.uri}",fallbackFactory= StoreFallbackFactory.class,path="orders/storage")
public interface StorageClient {
    @PostMapping("/incStorage")
    Boolean incStorage(@RequestParam("productCode") String productCode, @RequestParam(value = "optionCode",required=false)String optionCode, @RequestParam("incNum")int incNum);

    @GetMapping("/getStorageNum")
    long getStorageNumByProductCode(@RequestParam("productCode")String productCode);
}
