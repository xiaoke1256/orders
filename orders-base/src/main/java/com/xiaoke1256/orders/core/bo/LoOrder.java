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
@Table( name = "LO_ORDER")
public class LoOrder {
	@Id
	@Column(name = "lo_order_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loOrderId;
	
	@Column(name = "lo_order_no")
	private String loOrderNo;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "carriage_amt")
	private BigDecimal carriageAmt;

	@Column(name = "sub_order_no")
	private String subOrderNo;

	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;

	public Long getLoOrderId() {
		return loOrderId;
	}

	public void setLoOrderId(Long loOrderId) {
		this.loOrderId = loOrderId;
	}

	public String getLoOrderNo() {
		return loOrderNo;
	}

	public void setLoOrderNo(String loOrderNo) {
		this.loOrderNo = loOrderNo;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public BigDecimal getCarriageAmt() {
		return carriageAmt;
	}

	public void setCarriageAmt(BigDecimal carriageAmt) {
		this.carriageAmt = carriageAmt;
	}

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
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
