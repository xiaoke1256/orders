package com.xiaoke1256.orders.core.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 结算单
 * @author Administrator
 *
 */
public class SettleStatemt {
	
	private Long settleId;
	
	private String settleNo;
	
	private String year;
	
	private String month;
	
	private String storeNo;
	
	private String status;
	
	private BigDecimal totalAmt;
	
	private BigDecimal monthlyCharge;
	
	private BigDecimal commission;
	
	private BigDecimal otherCharge;
	
	private BigDecimal pendingPayment;

	private BigDecimal alreadyPaid;
	
	private String memo;
	
	private Timestamp insertTime;
	
	private Timestamp updateTime;
	
	public Long getSettleId() {
		return settleId;
	}
	public void setSettleId(Long settleId) {
		this.settleId = settleId;
	}
	public String getSettleNo() {
		return settleNo;
	}
	public void setSettleNo(String settleNo) {
		this.settleNo = settleNo;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
