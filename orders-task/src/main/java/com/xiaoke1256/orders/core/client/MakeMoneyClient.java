package com.xiaoke1256.orders.core.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;
import com.xiaoke1256.orders.core.dto.SettleStatemtQueryResultResp;

public interface MakeMoneyClient {
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
