package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 结算单项
 * @author Administrator
 *
 */
@Entity
@Table( name = "SETTLE_ITEM_ORDER")
public class SettleItemOrder {
	@Id
	@Column(name = "item_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;
	
	@ManyToOne
	@JoinColumn(name="settle_id")
	@Column(name = "settle_id", nullable = false)
	private SettleStatemt settleStatemt;
	
	@Column(name = "order_no")
	private String orderNo;
	
	@Column(name = "total_amt")
	private BigDecimal totalAmt;
	
	@Column(name = "commission")
	private BigDecimal commission;

	@Column(name = "other_charge")
	private BigDecimal otherCharge;
	
	@Column(name = "insert_time")
	private Timestamp insertTime;
	
	@Column(name = "update_time")
	private Timestamp updateTime;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public SettleStatemt getSettleStatemt() {
		return settleStatemt;
	}

	public void setSettleStatemt(SettleStatemt settleStatemt) {
		this.settleStatemt = settleStatemt;
	}

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

	public BigDecimal getCommission() {
		return commission;
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public BigDecimal getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(BigDecimal otherCharge) {
		this.otherCharge = otherCharge;
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
