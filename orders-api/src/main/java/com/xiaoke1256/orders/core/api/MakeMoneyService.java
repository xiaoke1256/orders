package com.xiaoke1256.orders.core.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemt;

public interface MakeMoneyService {
	/**
	 * 打款
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/makeMoney/pay",method= {RequestMethod.POST})
	public PayResp makeMoney(@RequestBody String settleNo);
	
	/**
	 * 查询待打款的结算单
	 */
	@RequestMapping(value="/settles/queryAwaitMakeMoney",method= {RequestMethod.GET})
	public List<SettleStatemt> queryAwaitMakeMoney(@RequestParam("year") String year,@RequestParam("month") String month);
	
}
