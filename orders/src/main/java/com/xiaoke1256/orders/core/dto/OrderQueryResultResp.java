package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.page.QueryResult;

public class OrderQueryResultResp extends QueryResultResp<SubOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrderQueryResultResp(QueryResult<SubOrder> queryResult) {
		super(queryResult);
	}

}
