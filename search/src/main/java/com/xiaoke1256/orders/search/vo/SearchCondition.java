package com.xiaoke1256.orders.search.vo;

import java.util.Map;

import com.xiaoke1256.orders.common.page.BaseCondition;

public class SearchCondition extends BaseCondition {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String searchName;
	
	private String userId;
	
	/**
	 * key is propertyName ; value is asc(True) or desc;
	 */
	private Map<String,Boolean> orderBy;
	
	private int pageNo;
	
	private int pageSize;

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Map<String, Boolean> getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Map<String, Boolean> orderBy) {
		this.orderBy = orderBy;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	

}
