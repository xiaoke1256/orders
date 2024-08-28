package com.xiaoke1256.orders.product.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

/**
 * 商品的相关服务
 * @author Administrator
 *
 */
public interface ProductQueryService {
	@RequestMapping(value="/product/search",method=RequestMethod.POST)
	public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition);
	
	@RequestMapping(value="/simpleProduct/{productCode}",method=RequestMethod.GET)
	public SimpleProduct getSimpleProductByCode(@PathVariable("productCode") String productCode);
}
