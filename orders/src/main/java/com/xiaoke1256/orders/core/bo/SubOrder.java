package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table( name = "SUB_ORDER")
public class SubOrder {
	/**订单状态：待支付*/
	public static final String ORDER_STATUS_PAYING = "0";
	/**订单状态：待卖家受理*/
	public static final String ORDER_STATUS_AWAIT_ACCEPT = "1";
	/**订单状态：待发货*/
	public static final String ORDER_STATUS_AWAIT_SENT = "2";
	/**订单状态：待清算*/
	public static final String ORDER_STATUS_AWAIT_SETTLE = "3";
	/**订单状态：待打款*/
	public static final String ORDER_STATUS_AWAIT_MAKE_MONEY = "4";
	/**订单状态：结束*/
	public static final String ORDER_STATUS_FINISH = "9";
	/**订单状态：取消*/
	public static final String ORDER_STATUS_CANCEL = "8";
	
	@Id
	@Column(name = "ORDER_NO", nullable = false)
	private String orderNo;
	@ManyToOne
	@JoinColumn(name="PAY_ORDER_ID")
	private PayOrder payOrder;
	@Column(name = "TOTAL_AMT")
	private BigDecimal totalAmt;
	@Column(name = "CARRIAGE_AMT")
	private BigDecimal  carriageAmt;
	@Column(name = "STORE_NO", nullable = false)
	private String storeNo;
	@Column(name = "STATUS")
	private String status="0";
	
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;
	
	@OneToMany(mappedBy = "subOrder",cascade = CascadeType.ALL, fetch = FetchType.LAZY )
	private Set<OrderItem> orderItems;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payOrder == null) ? 0 : payOrder.hashCode());
		result = prime * result + ((storeNo == null) ? 0 : storeNo.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
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
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		return true;
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
