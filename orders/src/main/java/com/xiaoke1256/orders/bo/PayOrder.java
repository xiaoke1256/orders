package com.xiaoke1256.orders.bo;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table( name = "PAY_ORDER")
public class PayOrder {
	@Id
	@Column(name = "PAY_ORDER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="payorder_seq")  
	@SequenceGenerator(name="payorder_seq", sequenceName="seq_payoder")  
	private Long payOrderId;
	@Column(name = "PAY_ORDER_NO", nullable = false)
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
