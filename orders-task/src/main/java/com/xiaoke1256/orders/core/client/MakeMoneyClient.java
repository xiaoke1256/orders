package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.api.MakeMoneyService;
import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemt;

import feign.hystrix.FallbackFactory;

@FeignClient(name="api-orders",url="${remote.api.orders.uri}",fallbackFactory=MakeMoneyFallbackFactory.class,path="orders")
public interface MakeMoneyClient extends MakeMoneyService {
	
}


class MakeMoneyFallbackFactory implements FallbackFactory<MakeMoneyService>{
	private static final Logger logger = LoggerFactory.getLogger(MakeMoneyFallbackFactory.class);
	@Override
	public MakeMoneyService create(Throwable cause) {
		return new MakeMoneyService() {

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