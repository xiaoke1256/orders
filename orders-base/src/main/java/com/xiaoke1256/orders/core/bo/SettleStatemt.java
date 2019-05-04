package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 结算单
 * @author Administrator
 *
 */
@Entity
@Table( name = "SETTLE_STATEMT")
public class SettleStatemt {
	@Id
	@Column(name = "settle_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long settleId;
	
	@Column(name = "year")
	private String year;
	
	@Column(name = "month")
	private String month;
	
	@Column(name = "store_no")
	private String storeNo;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "total_amt")
	private BigDecimal totalAmt;
	
	@Column(name = "monthly_charge")
	private BigDecimal monthlyCharge;
	
	@Column(name = "commission")
	private BigDecimal commission;
	
	@Column(name = "other_charge")
	private BigDecimal otherCharge;
	
	@Column(name = "pending_payment")
	private BigDecimal pendingPayment;
	
	@Column(name = "already_paid")
	private BigDecimal alreadyPaid;
	
	@Column(name = "insert_time")
	private Timestamp insertTime;
	
	@Column(name = "update_time")
	private Timestamp updateTime;
	
	public Long getSettleId() {
		return settleId;
	}
	public void setSettleId(Long settleId) {
		this.settleId = settleId;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public BigDecimal getMonthlyCharge() {
		return monthlyCharge;
	}
	public void setMonthlyCharge(BigDecimal monthlyCharge) {
		this.monthlyCharge = monthlyCharge;
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
	public BigDecimal getPendingPayment() {
		return pendingPayment;
	}
	public void setPendingPayment(BigDecimal pendingPayment) {
		this.pendingPayment = pendingPayment;
	}
	public BigDecimal getAlreadyPaid() {
		return alreadyPaid;
	}
	public void setAlreadyPaid(BigDecimal alreadyPaid) {
		this.alreadyPaid = alreadyPaid;
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
