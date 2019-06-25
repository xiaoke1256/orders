package com.xiaoke1256.orders.core.client;

import java.util.Map;

import org.springframework.stereotype.Component;
//import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

//@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=ProductQueryFallbackFactory.class,path="product")
@Component
public class ProductQueryClient implements ProductQueryService {

	@Override
	public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleProduct getSimpleProductByCode(String productCode) {
		// TODO Auto-generated method stub
		return null;
	}
}
