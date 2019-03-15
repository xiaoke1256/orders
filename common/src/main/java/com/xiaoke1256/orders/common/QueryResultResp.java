package com.xiaoke1256.orders.common;

import java.util.List;

import com.xiaoke1256.orders.common.page.QueryResult;

public class QueryResultResp extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public QueryResultResp() {
		super();
	}

	public QueryResultResp(String code, String msg, QueryResult<?> queryResult) {
		super(code, msg);
		this.queryResult=queryResult;
	}

	private QueryResult<?> queryResult;

	public int getPageNo() {
		return queryResult.getPageNo();
	}

	public int getPageSize() {
		return queryResult.getPageSize();
	}

	public int getTotalCount() {
		return queryResult.getTotalCount();
	}

	public int getTotalPages() {
		return queryResult.getTotalPages();
	}

	public List<?> getResultList() {
		return queryResult.getResultList();
	}

}
