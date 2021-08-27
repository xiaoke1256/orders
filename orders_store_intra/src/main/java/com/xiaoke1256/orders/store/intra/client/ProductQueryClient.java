package com.xiaoke1256.orders.store.intra.client;

import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=MemberQueryFallbackFactory.class,path="product")
public interface ProductQueryClient extends ProductQueryService {
    @GetMapping("/product/search")
    SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition);

    @GetMapping("/simpleProduct/{productCode}")
    SimpleProduct getSimpleProductByCode(@PathVariable String productCode);
}
