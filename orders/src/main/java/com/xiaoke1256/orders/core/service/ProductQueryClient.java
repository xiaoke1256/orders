package com.xiaoke1256.orders.core.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.ProductCondition;

public interface ProductQueryClient extends ProductQueryService {

	@RequestMapping(value="/product/search",method=RequestMethod.GET)
	public RespMsg searchProductByCondition(ProductCondition condition);

}
