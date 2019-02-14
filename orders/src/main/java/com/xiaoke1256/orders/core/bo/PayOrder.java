package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table( name = "PAY_ORDER")
public class PayOrder {
	@Id
	@Column(name = "pay_order_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long payOrderId;
	
	@Column(name = "PAY_ORDER_NO")
	private String payOrderNo;
	@Column(name = "TOTAL_AMT")
	private BigDecimal totalAmt;
	@Column(name = "CARRIAGE_AMT")
	private BigDecimal  carriageAmt;
	@Column(name = "PAYER_NO")
	private String payerNo;
	@OneToMany(mappedBy = "payOrder",cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	private Set<SubOrder> subOrders;
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payOrderId == null) ? 0 : payOrderId.hashCode());
		result = prime * result + ((payOrderNo == null) ? 0 : payOrderNo.hashCode());
		result = prime * result + ((payerNo == null) ? 0 : payerNo.hashCode());
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
		PayOrder other = (PayOrder) obj;
		if (payOrderId == null) {
			if (other.payOrderId != null)
				return false;
		} else if (!payOrderId.equals(other.payOrderId))
			return false;
		if (payOrderNo == null) {
			if (other.payOrderNo != null)
				return false;
		} else if (!payOrderNo.equals(other.payOrderNo))
			return false;
		if (payerNo == null) {
			if (other.payerNo != null)
				return false;
		} else if (!payerNo.equals(other.payerNo))
			return false;
		return true;
	}
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
	
}
