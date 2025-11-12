package com.xiaoke1256.thirdpay.payplatform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

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
	private String palteform;
	private String incident;
	private String remark;
	private Date insertTime;
	private String orderStatus;
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
	public String getPalteform() {
		return palteform;
	}
	public void setPalteform(String palteform) {
		this.palteform = palteform;
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
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
}
