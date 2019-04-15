package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.page.QueryResult;

public class PayOrderQueryResultResp extends QueryResultResp<PayOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PayOrderQueryResultResp(QueryResult<PayOrder> queryResult) {
		super(queryResult);
	}

}
