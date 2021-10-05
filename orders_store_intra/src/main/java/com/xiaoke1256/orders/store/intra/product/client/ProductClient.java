package com.xiaoke1256.orders.store.intra.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory= ProductQueryFallbackFactory.class,path="product")
public interface ProductClient {
    /**
     * 修改上下架状态
     * @param productCode 商品编号
     */
    @RequestMapping(path="/product/{productCode}/switchShfs",method = {RequestMethod.GET})
    void switchShelves(@PathVariable("productCode") String productCode, @RequestParam String onOrOff);
}
