package com.xiaoke1256.orders.core.controller;

import java.math.BigDecimal;
import java.util.Map;

public class OrderPlaceRequest implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String payerNo;
	private BigDecimal carriageAmt;
	private Map<String,Integer> productMap;
	public String getPayerNo() {
		return payerNo;
	}
	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}
	public BigDecimal getCarriageAmt() {
		return carriageAmt;
	}
	public void setCarriageAmt(BigDecimal carriageAmt) {
		this.carriageAmt = carriageAmt;
	}
	public Map<String, Integer> getProductMap() {
		return productMap;
	}
	public void setProductMap(Map<String, Integer> productMap) {
		this.productMap = productMap;
	}
	
	
}
