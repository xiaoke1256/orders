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
@Table( name = "HOUSEHOLD_ACC_TXN")
public class HouseholdAccTxn {
	@Id
	@Column(name = "TXN_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long txnId;
	
	@Column(name = "ACC_NO")
	private String accNo;
	
	@Column(name = "ACC_FLG")
	private String accFlg;
	
	@Column(name = "pay_order_no")
	private String payOrderNo;
	
	@Column(name = "sub_order_no")
	private String subOrderNo;
	
	@Column(name = "IS_CURRENT")
	private String isCurrent;
	
	@Column(name = "amt")
	private BigDecimal amt;
	
	@Column(name = "cash_balance")
	private BigDecimal cashBalance;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "insert_time")
	private Timestamp insertTime;
	
	@Column(name = "update_time")
	private Timestamp updateTime;

	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccFlg() {
		return accFlg;
	}

	public void setAccFlg(String accFlg) {
		this.accFlg = accFlg;
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

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getCashBalance() {
		return cashBalance;
	}

	public void setCashBalance(BigDecimal cashBalance) {
		this.cashBalance = cashBalance;
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
	
	
}
