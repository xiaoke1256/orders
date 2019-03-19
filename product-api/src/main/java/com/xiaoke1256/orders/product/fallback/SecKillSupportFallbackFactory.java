package com.xiaoke1256.orders.product.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.product.api.SecKillSupportService;

import feign.hystrix.FallbackFactory;

public class SecKillSupportFallbackFactory implements FallbackFactory<SecKillSupportService> {
	
	private static final Logger logger = LoggerFactory.getLogger(SecKillSupportFallbackFactory.class);

	@Override
	public SecKillSupportService create(Throwable arg0) {
		return new SecKillSupportService() {

			@Override
			public RespMsg openSecKill(String productCode) {
				logger.error("connect fail.by hystrix.");
				return new ErrMsg(ErrorCode.CONNECT_ERROR);
			}

			@Override
			public RespMsg closeSecKill(String productCode) {
				logger.error("connect fail.by hystrix.");
				return new ErrMsg(ErrorCode.CONNECT_ERROR);
			}
			
		};
	}

}
