package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.page.QueryResult;

public class SettleStatemtQueryResultResp extends QueryResultResp<SettleStatemt> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SettleStatemtQueryResultResp() {
		super();
	}

	public SettleStatemtQueryResultResp(QueryResult<SettleStatemt> queryResult) {
		super(queryResult);
	}

	public SettleStatemtQueryResultResp(RespCode errorCode) {
		super(errorCode);
	}

	public SettleStatemtQueryResultResp(String code, String msg, QueryResult<SettleStatemt> queryResult) {
		super(code, msg, queryResult);
	}

	public SettleStatemtQueryResultResp(String code, String msg) {
		super(code, msg);
	}
	
	

}
