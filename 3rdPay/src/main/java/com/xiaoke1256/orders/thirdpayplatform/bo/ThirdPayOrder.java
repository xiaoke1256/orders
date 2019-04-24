package com.xiaoke1256.orders.thirdpayplatform.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 第三方支付平台的订单
 *
 */
public class ThirdPayOrder {
	
	/**状态：刚刚接收到订单*/
	public static final String STATUS_ACCEPT = "00";
	
	/**状态：订单处理成功*/
	public static final String STATUS_SUCCESS = "90";
	
	/**状态：订单处理失败*/
	public static final String STATUS_FAIL = "99";
	
	/**状态：订单处理超时失败*/
	public static final String STATUS_EXPIRED = "98";
	
	/**状态：订单处理失败，需人工处理*/
	public static final String STATUS_NEED_MANNUAL = "95";
	
	private Long orderId;
	
	private String orderNo;
	
	private String orderType;
	
	private String orderStatus="00";
	
	private String payerNo;
	
	private String payeeNo;
	
	private BigDecimal amt;

	/**接入平台*/
	private String palteform;
	
	private String incident;

	private String remark;
	
	private Timestamp insertTime;
	
	private Timestamp updateTime;
	
	private Timestamp finishTime;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

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
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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

	public Timestamp getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}
	
	

}
