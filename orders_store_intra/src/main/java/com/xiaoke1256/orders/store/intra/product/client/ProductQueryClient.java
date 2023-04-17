package com.xiaoke1256.orders.store.intra.product.client;

import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory= ProductQueryFallbackFactory.class,path="product")
public interface ProductQueryClient extends ProductQueryService {
    @RequestMapping(value="/product/search",method = {RequestMethod.GET},consumes = "application/json")
    public SimpleProductQueryResultResp searchProductByCondition(@RequestBody ProductCondition condition);

    @GetMapping("/simpleProduct/{productCode}")
    public SimpleProduct getSimpleProductByCode(@PathVariable("productCode") String productCode);
}
