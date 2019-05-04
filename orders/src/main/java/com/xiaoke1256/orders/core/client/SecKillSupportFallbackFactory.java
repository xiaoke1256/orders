package com.xiaoke1256.orders.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;

import feign.hystrix.FallbackFactory;

@Component
public class SecKillSupportFallbackFactory implements FallbackFactory<SecKillSupportClient> {
	
	private static final Logger logger = LoggerFactory.getLogger(SecKillSupportFallbackFactory.class);

	@Override
	public SecKillSupportClient create(Throwable cause) {
		return new SecKillSupportClient() {

			@Override
			public RespMsg openSecKill(String productCode) {
				logger.error("connect fail.by hystrix.",cause);
				return new RespMsg(RespCode.CONNECT_ERROR);
			}

			@Override
			public RespMsg closeSecKill(String productCode) {
				logger.error("connect fail.by hystrix.",cause);
				return new RespMsg(RespCode.CONNECT_ERROR);
			}
			
		};
	}

}
