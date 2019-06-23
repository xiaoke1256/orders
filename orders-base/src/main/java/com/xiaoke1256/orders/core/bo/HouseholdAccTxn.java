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
 * 分户账支付流水表
 * @author Administrator
 *
 */
@Entity
@Table( name = "HOUSEHOLD_ACC_TXN")
public class HouseholdAccTxn {
	@Id
	@Column(name = "TXN_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long txnId;
	
	@Column(name = "PAYMENT_ID")
	private Long paymentId;	
	/**
	 * 账户号（即各个商户号，18个‘0’表示平台方） 
	 */
	@Column(name = "ACC_NO")
	private String accNo;
	
	/**
	 * 借贷符号(+表示借，-表示贷)
	 */
	@Column(name = "ACC_FLG")
	private String accFlg;
	
	/**
	 * 支付单号
	 */
	@Column(name = "pay_order_no")
	private String payOrderNo;
	
	/**
	 * 订单号
	 */
	@Column(name = "sub_order_no")
	private String subOrderNo;
	
	/**
	 * 是否最后一笔流水
	 */
	@Column(name = "IS_CURRENT")
	private String isCurrent;
	
	/**
	 * 支付额
	 */
	@Column(name = "amt")
	private BigDecimal amt;
	
	/**
	 * 现金余额
	 */
	@Column(name = "cash_balance")
	private BigDecimal cashBalance;
	
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	
	/**
	 * 插入时间
	 */
	@Column(name = "insert_time")
	private Timestamp insertTime;
	
	/**
	 * 修改时间
	 */
	@Column(name = "update_time")
	private Timestamp updateTime;

	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
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
