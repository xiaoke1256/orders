package com.xiaoke1256.orders.search.vo;

import java.io.Serializable;
import java.util.List;

import com.xiaoke1256.orders.search.common.ErrMsg;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ErrMsg error;
	
	private int pageNo;
	
	private int pageSize;
	
	private int totalCount;
	
	private int totalPages;
	
	private List<Product> resultList;
	

	public SearchResult(int pageNo, int pageSize, int totalCount, List<Product> resultList) {
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

	public SearchResult() {
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

	public boolean hasNext() {
		return pageNo<totalPages;
	}


	public boolean hasPrev() {
		return pageNo>0;
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

	public ErrMsg getError() {
		return error;
	}

	public void setError(ErrMsg error) {
		this.error = error;
	}
	
	
}
