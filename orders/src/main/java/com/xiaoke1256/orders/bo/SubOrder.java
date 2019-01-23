package com.xiaoke1256.orders.bo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "SUB_ORDER")
public class SubOrder {
	@Id
	@Column(name = "SUB_ORDER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long subOrderId;
	@ManyToOne
	@JoinColumn(name="PAY_ORDER_NO")
	private PayOrder payOrder;
	@Column(name = "TOTAL_AMT")
	private BigDecimal totalAmt;
	@Column(name = "CARRIAGE_AMT")
	private BigDecimal  carriageAmt;
	@Column(name = "STORE_NO", nullable = false)
	private String storeNo;
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payOrder == null) ? 0 : payOrder.hashCode());
		result = prime * result + ((storeNo == null) ? 0 : storeNo.hashCode());
		result = prime * result + ((subOrderId == null) ? 0 : subOrderId.hashCode());
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
		SubOrder other = (SubOrder) obj;
		if (payOrder == null) {
			if (other.payOrder != null)
				return false;
		} else if (!payOrder.equals(other.payOrder))
			return false;
		if (storeNo == null) {
			if (other.storeNo != null)
				return false;
		} else if (!storeNo.equals(other.storeNo))
			return false;
		if (subOrderId == null) {
			if (other.subOrderId != null)
				return false;
		} else if (!subOrderId.equals(other.subOrderId))
			return false;
		return true;
	}
	public Long getSubOrderId() {
		return subOrderId;
	}
	public void setSubOrderId(Long subOrderId) {
		this.subOrderId = subOrderId;
	}
	public PayOrder getPayOrder() {
		return payOrder;
	}
	public void setPayOrder(PayOrder payOrder) {
		this.payOrder = payOrder;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
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
	
}
