package com.xiaoke1256.orders.dto;

import java.math.BigDecimal;
import java.util.Set;

public class SubOrder {

	private String orderNo;

	private BigDecimal totalAmt;

	private BigDecimal  carriageAmt;

	private String storeNo;
	
	private Set<OrderItem> orderItems;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	
}
