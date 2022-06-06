package com.xiaoke1256.orders.common;

import java.util.List;

import com.xiaoke1256.orders.common.page.QueryResult;

public class QueryResultResp<T> extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int pageNo;
	
	private int pageSize;
	
	private int totalCount;
	
	private int totalPages;
	
	private List<T> resultList;
	

	public QueryResultResp() {
		super();
	}
	
	public QueryResultResp(RespCode errorCode) {
		super(errorCode);
	}

	public QueryResultResp(String code, String msg) {
		super(code, msg);
	}

	public QueryResultResp(QueryResult<T> queryResult) {
		super(RespCode.SUCCESS);
		this.pageNo = queryResult.getPageNo();
		this.pageSize = queryResult.getPageSize();
		this.totalCount = queryResult.getTotalCount();
		this.totalPages = queryResult.getTotalPages();
		this.resultList = queryResult.getResultList();
	}

	public QueryResultResp(String code, String msg, QueryResult<T> queryResult) {
		super(code, msg);
		if(queryResult!=null) {
			this.pageNo = queryResult.getPageNo();
			this.pageSize = queryResult.getPageSize();
			this.totalCount = queryResult.getTotalCount();
			this.totalPages = queryResult.getTotalPages();
			this.resultList = queryResult.getResultList();
		}
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}

	

}
