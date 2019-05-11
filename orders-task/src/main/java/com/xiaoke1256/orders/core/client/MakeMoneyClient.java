package com.xiaoke1256.orders.core.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.core.api.MakeMoneyService;
import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;
import com.xiaoke1256.orders.core.dto.SettleStatemtQueryResultResp;

import feign.hystrix.FallbackFactory;

@FeignClient(name="api-orders",url="${remote.api.orders.uri}",fallbackFactory=MakeMoneyFallbackFactory.class,path="orders")
public interface MakeMoneyClient extends MakeMoneyService {
	/**
	 * 打款
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/makeMoney/pay",method= {RequestMethod.POST})
	public com.xiaoke1256.orders.core.dto.PayResp makeMoney(@RequestBody String settleNo);
	
	/**
	 * 查询
	 */
	@RequestMapping(value="/settles/search",method= {RequestMethod.GET})
	public SettleStatemtQueryResultResp searchSettle(SettleStatemtCondition condition);
	
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
			public SettleStatemtQueryResultResp searchSettle(SettleStatemtCondition condition) {
				logger.error("connect fail.by hystrix.",cause);
				return new SettleStatemtQueryResultResp(RespCode.CONNECT_ERROR);
			}};
	}
	
}