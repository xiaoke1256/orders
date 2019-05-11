package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.page.BaseCondition;

public class SettleStatemtCondition extends BaseCondition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String settleNo;
	
	private String year;
	
	private String month;
	
	private String storeNo;
	
	private String status;

	public String getSettleNo() {
		return settleNo;
	}

	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
