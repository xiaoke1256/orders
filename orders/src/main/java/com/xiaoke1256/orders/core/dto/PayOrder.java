package com.xiaoke1256.orders.core.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import com.xiaoke1256.orders.core.dto.SubOrder;

public class PayOrder {

	private Long payOrderId;
	
	private String payOrderNo;

	private BigDecimal  carriageAmt;
	
	private BigDecimal totalAmt;

	private String payerNo;
	
	private Set<SubOrder> subOrders;

	private Timestamp insertTime;

	private Timestamp updateTime;
	
	private String productCodes;

	private String status;


	public Long getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public BigDecimal getCarriageAmt() {
		return carriageAmt;
	}

	public void setCarriageAmt(BigDecimal carriageAmt) {
		this.carriageAmt = carriageAmt;
	}

	public BigDecimal getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getPayerNo() {
		return payerNo;
	}

	public void setPayerNo(String payerNo) {
		this.payerNo = payerNo;
	}

	public Set<SubOrder> getSubOrders() {
		return subOrders;
	}

	public void setSubOrders(Set<SubOrder> subOrders) {
		this.subOrders = subOrders;
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

	public String getProductCodes() {
		return productCodes;
	}

	public void setProductCodes(String productCodes) {
		this.productCodes = productCodes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
