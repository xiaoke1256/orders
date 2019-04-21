package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "PAYMENT_TXN")
public class PaymentTxn {
	/**
	 * 处理状态：待处理
	 */
	public static final String DEAL_STATUS_INIT = "0";
	
	/**
	 * 处理状态：已记录分户账
	 */
	public static final String DEAL_STATUS_HOUSEHOLD = "1";
	@Id
	@Column(name = "PAYMENT_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	
	@Column(name = "payer_no")
	private String payerNo;
	
	@Column(name = "payee_no")
	private String payeeNo;
	
	@Column(name = "pay_type")
	private String payType;
	
	@Column(name = "amt")
	private BigDecimal amt;

	@Column(name = "pay_order_no")
	private String payOrderNo;
	
	@Column(name = "sub_order_no")
	private String subOrderNo;
	
	@Column(name = "third_order_no")
	private String thirdOrderNo;
	
	@Column(name = "REVERSE_FLG")
	private String reverseFlg;
	
	@Column(name = "incident")
	private String incident;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "deal_status")
	private String dealStatus="0";
	
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getThirdOrderNo() {
		return thirdOrderNo;
	}

	public void setThirdOrderNo(String thirdOrderNo) {
		this.thirdOrderNo = thirdOrderNo;
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

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
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
	

	public String getReverseFlg() {
		return reverseFlg;
	}

	public void setReverseFlg(String reverseFlg) {
		this.reverseFlg = reverseFlg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payType == null) ? 0 : payType.hashCode());
		result = prime * result + ((payeeNo == null) ? 0 : payeeNo.hashCode());
		result = prime * result + ((payerNo == null) ? 0 : payerNo.hashCode());
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentTxn other = (PaymentTxn) obj;
		if (payType == null) {
			if (other.payType != null)
				return false;
		} else if (!payType.equals(other.payType))
			return false;
		if (payeeNo == null) {
			if (other.payeeNo != null)
				return false;
		} else if (!payeeNo.equals(other.payeeNo))
			return false;
		if (payerNo == null) {
			if (other.payerNo != null)
				return false;
		} else if (!payerNo.equals(other.payerNo))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		return true;
	}
	
	
}
