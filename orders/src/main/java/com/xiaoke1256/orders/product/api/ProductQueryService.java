package com.xiaoke1256.orders.product.api;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.product.dto.ProductCondition;

/**
 * 商品查询服务
 * @author Administrator
 *
 */
public interface ProductQueryService {
	public RespMsg searchProductByCondition(ProductCondition condition);
}
