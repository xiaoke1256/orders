package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.api.MakeMoneyService;
import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemt;

//import feign.hystrix.FallbackFactory;

@Component
public class MakeMoneyFallbackFactory /*implements FallbackFactory<MakeMoneyClient>*/{
	private static final Logger logger = LoggerFactory.getLogger(MakeMoneyFallbackFactory.class);
	//@Override
	public MakeMoneyClient create(Throwable cause) {
		return new MakeMoneyClient() {

			@Override
			public PayResp makeMoney(String settleNo) {
				logger.error("connect fail.by hystrix.",cause);
				return new PayResp(RespCode.CONNECT_ERROR);
			}

			@Override
			public List<SettleStatemt> queryAwaitMakeMoney(String year, String month) {
				logger.error("connect fail.by hystrix.",cause);
				throw new AppException(RespCode.CONNECT_ERROR);
			}
		};

	}
	
}