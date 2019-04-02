package com.xiaoke1256.orders.core.controller;

import java.math.BigDecimal;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;

public class OrderPlaceResponse extends RespMsg implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public OrderPlaceResponse() {
		super(ErrorCode.SUCCESS);
	}
	public OrderPlaceResponse(ErrorCode errorCode) {
		super(errorCode);
	}
	public OrderPlaceResponse(String code, String msg) {
		super(code, msg);
	}
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
