package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.page.BaseCondition;

public class PayOrderCondition extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String payOrderNo;
	
	private String payerNo;
	
	private String status;

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getPayerNo() {
		return payerNo;
	}

	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
