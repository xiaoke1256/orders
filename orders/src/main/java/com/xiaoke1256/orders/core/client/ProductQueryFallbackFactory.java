package com.xiaoke1256.orders.core.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

import feign.hystrix.FallbackFactory;

@Component
public class ProductQueryFallbackFactory implements FallbackFactory<ProductQueryClient> {

	private static final Logger logger = LoggerFactory.getLogger(ProductQueryFallbackFactory.class);
	@Override
	public ProductQueryClient create(Throwable cause) {
		
		return new ProductQueryClient() {

			@Override
			public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition) {
				logger.error("connect fail.by hystrix.",cause);
				return new SimpleProductQueryResultResp(RespCode.CONNECT_ERROR);
			}

			@Override
			public SimpleProduct getSimpleProductByCode(String productCode) {
				logger.error("connect fail.by hystrix.",cause);
				throw new AppException(RespCode.CONNECT_ERROR);
			}
			
		};
	}

}
