package com.xiaoke1256.orders.product.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.ProductCondition;

import feign.hystrix.FallbackFactory;

public class ProductQueryFallbackFactory implements FallbackFactory<ProductQueryService> {

	private static final Logger logger = LoggerFactory.getLogger(SecKillSupportFallbackFactory.class);
	@Override
	public ProductQueryService create(Throwable arg0) {
		
		return new ProductQueryService() {

			@Override
			public QueryResultResp searchProductByCondition(ProductCondition condition) {
				logger.error("connect fail.by hystrix.");
				return new QueryResultResp(ErrorCode.CONNECT_ERROR);
			}
			
		};
	}

}
