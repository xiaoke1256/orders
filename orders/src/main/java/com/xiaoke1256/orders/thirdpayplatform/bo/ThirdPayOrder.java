package com.xiaoke1256.orders.thirdpayplatform.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "THIRD_PAY_ORDER")
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
	
	@Id
	@Column(name = "ORDER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;
	
	@Column(name = "ORDER_NO")
	private String orderNo;
	
	@Column(name = "ORDER_TYPE")
	private String orderType;
	
	@Column(name = "ORDER_STATUS")
	private String orderStatus="00";
	
	@Column(name = "payer_no")
	private String payerNo;
	
	@Column(name = "payee_no")
	private String payeeNo;
	
	@Column(name = "amt")
	private BigDecimal amt;

	@Column(name = "palteform")
	private String palteform;
	
	@Column(name = "incident")
	private String incident;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;
	
	@Column(name = "FINISH_TIME")
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
