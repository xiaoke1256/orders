package com.xiaoke1256.orders.common.page;

import java.io.Serializable;

public abstract class BaseCondition implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int pageNo=1;
	
	private int pageSize=10;

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
	
	
}
