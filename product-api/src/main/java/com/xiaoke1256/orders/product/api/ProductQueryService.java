package com.xiaoke1256.orders.product.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

/**
 * 商品的相关服务
 * @author Administrator
 *
 */
public interface ProductQueryService {
	@RequestMapping(value="/product/search",method=RequestMethod.GET,consumes="application/json")
	public SimpleProductQueryResultResp searchProductByCondition(@RequestBody ProductCondition condition);
}
