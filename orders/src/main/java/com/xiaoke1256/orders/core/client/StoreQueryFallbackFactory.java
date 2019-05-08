package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.product.dto.Store;

import feign.hystrix.FallbackFactory;

public class StoreQueryFallbackFactory implements FallbackFactory<StoreQueryClient> {
	private static final Logger logger = LoggerFactory.getLogger(StoreQueryFallbackFactory.class);

	@Override
	public StoreQueryClient create(Throwable cause) {
		return new StoreQueryClient() {

			@Override
			public List<Store> queryAvailableStore() {
				logger.error("connect fail.by hystrix.",cause);
				throw new AppException(RespCode.CONNECT_ERROR);
			}

			@Override
			public Store getStore(String storeNo) {
				logger.error("connect fail.by hystrix.",cause);
				throw new AppException(RespCode.CONNECT_ERROR);
			}
			
		};
	}

}
