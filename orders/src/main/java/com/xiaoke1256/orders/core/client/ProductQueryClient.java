package com.xiaoke1256.orders.core.client;

import java.util.Map;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

@FeignClient(name="api-product",
        url="${remote.api.product.uri}",
        fallbackFactory=ProductQueryFallbackFactory.class,
        contextId="ProductQuery")
public interface ProductQueryClient extends ProductQueryService {
}
