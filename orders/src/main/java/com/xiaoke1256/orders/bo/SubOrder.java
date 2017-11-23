package com.xiaoke1256.orders.bo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "SUB_ORDER")
public class SubOrder {
	@Id
	@Column(name = "SUB_ORDER_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="suborder_seq")  
	@SequenceGenerator(name="suborder_seq", sequenceName="seq_suboder")  
	private Long subOrderId;
	@ManyToOne
	@JoinColumn(name="PAY_ORDER_ID")
	private PayOrder payOrder;
	@Column(name = "STORE_NO", nullable = false)
	private String storeNo;
	@ManyToOne
	@JoinColumn(name="PRODUCT_CODE")
	private Product product;
	@Column(name = "PRODUCT_NUM")
	private Integer productNum;
	@Column(name = "PRODUCT_PRICE")
	private BigDecimal productPrice;
	
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
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getProductNum() {
		return productNum;
	}
	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	
	
}
