package com.xiaoke1256.orders.product.api;

import org.springframework.web.bind.annotation.GetMapping;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.product.dto.ProductCondition;

/**
 * 商品的相关服务
 * @author Administrator
 *
 */
public interface ProductQueryService {
	@GetMapping("/product/search")
	public RespMsg searchProductByCondition(ProductCondition condition);
}
