package com.xiaoke1256.orders.core.controller;

import java.math.BigDecimal;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;

public class OrderPlaceResponse extends RespMsg implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String payOrderNo;
	private BigDecimal totalAmt;
	private BigDecimal  carriageAmt;
	private String payerNo;
	
	public String getPayOrderNo() {
		return payOrderNo;
	}
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public BigDecimal getCarriageAmt() {
		return carriageAmt;
	}
	public void setCarriageAmt(BigDecimal carriageAmt) {
		this.carriageAmt = carriageAmt;
	}
	public String getPayerNo() {
		return payerNo;
	}
	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}
	
}
