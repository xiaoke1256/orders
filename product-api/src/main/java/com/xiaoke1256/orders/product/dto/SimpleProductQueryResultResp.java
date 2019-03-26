package com.xiaoke1256.orders.product.dto;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.common.page.QueryResult;

public class SimpleProductQueryResultResp extends QueryResultResp<SimpleProduct> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimpleProductQueryResultResp() {
		super();
	}

	public SimpleProductQueryResultResp(ErrorCode errorCode) {
		super(errorCode);
	}

	public SimpleProductQueryResultResp(QueryResult<SimpleProduct> queryResult) {
		super(queryResult);
	}

	public SimpleProductQueryResultResp(String code, String msg, QueryResult<SimpleProduct> queryResult) {
		super(code, msg, queryResult);
	}

	public SimpleProductQueryResultResp(String code, String msg) {
		super(code, msg);
	}
	
	
	

}
