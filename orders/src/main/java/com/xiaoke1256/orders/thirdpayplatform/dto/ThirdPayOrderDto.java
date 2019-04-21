package com.xiaoke1256.orders.thirdpayplatform.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ThirdPayOrderDto implements java.io.Serializable {
	
	/** 订单类型：消费*/
	public static final String ORDER_TYPE_CONSUME = "01";
	/** 订单类型：退货*/
	public static final String ORDER_TYPE_RETURN = "02";
	/** 订单类型：与平台方结算*/
	public static final String ORDER_TYPE_SETTLE = "03";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderNo;
	private String orderType;
	private String payerNo;
	private String payeeNo;
	private BigDecimal amt;
	private String incident;
	private String remark;
	private Timestamp insertTime;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPayerNo() {
		return payerNo;
	}
	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}
	public String getPayeeNo() {
		return payeeNo;
	}
	public void setPayeeNo(String payeeNo) {
		this.payeeNo = payeeNo;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getIncident() {
		return incident;
	}
	public void setIncident(String incident) {
		this.incident = incident;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Timestamp getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}
	
	
}
