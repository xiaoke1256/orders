package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;

public class PayConfig extends RespMsg {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String pateformPayAccount;
    
	private String thirdpaySiteUrl;
	
	
	public PayConfig(String pateformPayAccount,String thirdpaySiteUrl) {
		super(RespCode.SUCCESS);
		this.pateformPayAccount = pateformPayAccount;
		this.thirdpaySiteUrl = thirdpaySiteUrl;
	}
	public String getPateformPayAccount() {
		return pateformPayAccount;
	}
	public void setPateformPayAccount(String pateformPayAccount) {
		this.pateformPayAccount = pateformPayAccount;
	}
	public String getThirdpaySiteUrl() {
		return thirdpaySiteUrl;
	}
	public void setThirdpaySiteUrl(String thirdpaySiteUrl) {
		this.thirdpaySiteUrl = thirdpaySiteUrl;
	}
	
	
	
}
