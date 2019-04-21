package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;

public class PayConfig extends RespMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pateformPayAccount;
	
	
	public PayConfig(String pateformPayAccount) {
		super(ErrorCode.SUCCESS);
		this.pateformPayAccount = pateformPayAccount;
	}
	public String getPateformPayAccount() {
		return pateformPayAccount;
	}
	public void setPateformPayAccount(String pateformPayAccount) {
		this.pateformPayAccount = pateformPayAccount;
	}
	
	
}
