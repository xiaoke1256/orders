package com.xiaoke1256.orders.core.api;

import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;
import com.xiaoke1256.orders.core.dto.SettleStatemtQueryResultResp;

public interface MakeMoneyService {
	/**
	 * 打款
	 * @param settleNo
	 * @return
	 */
	public PayResp makeMoney(String settleNo);
	/**
	 * 按条件搜索
	 * @param condition
	 * @return
	 */
	public SettleStatemtQueryResultResp searchSettle(SettleStatemtCondition condition);
}
