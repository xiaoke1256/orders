package com.xiaoke1256.orders.search.vo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int pageNo;
	
	private int pageSize;
	
	private int totalCount;
	
	private boolean hasNext;
	
	private boolean hasPrev;
	
	private List<Product> resultList;

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

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	public List<Product> getResultList() {
		return resultList;
	}

	public void setResultList(List<Product> resultList) {
		this.resultList = resultList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
