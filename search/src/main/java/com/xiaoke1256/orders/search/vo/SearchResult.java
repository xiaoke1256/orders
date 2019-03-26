package com.xiaoke1256.orders.search.vo;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.page.QueryResult;

public class SearchResult extends QueryResultResp<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchResult(String code, String msg, QueryResult<Product> queryResult) {
		super(code, msg, queryResult);
	}
}
