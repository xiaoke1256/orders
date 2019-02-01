package com.xiaoke1256.orders.common.page;

import java.io.Serializable;
import java.util.List;

public class QueryResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int pageNo;
	
	private int pageSize;
	
	private int totalCount;
	
	private int totalPages;
	
	private List<T> resultList;

	public QueryResult() {
		super();
	}
	
	public QueryResult(int pageNo, int pageSize, int totalCount) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		if(totalCount<=0)
			totalPages = 1;
		else
			totalPages = (totalCount-1)/pageSize+1;
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
	
	public QueryResult(int pageNo, int pageSize, int totalCount, List<T> resultList) {
		super();
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.resultList = resultList;
		if(pageSize>0)
			this.totalPages=totalCount/pageSize+(totalCount%pageSize>0?1:0);
		else
			this.totalPages=1;
		if(totalPages>0 && pageNo>totalPages)
			throw new RuntimeException("Out of page bound.");
	}
}
