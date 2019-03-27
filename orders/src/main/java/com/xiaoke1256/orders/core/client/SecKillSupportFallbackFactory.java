package com.xiaoke1256.orders.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;

import feign.hystrix.FallbackFactory;

@Component
public class SecKillSupportFallbackFactory implements FallbackFactory<SecKillSupportClient> {
	
	private static final Logger logger = LoggerFactory.getLogger(SecKillSupportFallbackFactory.class);

	@Override
	public SecKillSupportClient create(Throwable arg0) {
		return new SecKillSupportClient() {

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
