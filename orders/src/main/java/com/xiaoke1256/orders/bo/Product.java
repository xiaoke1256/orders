package com.xiaoke1256.orders.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "PRODUCT")
public class Product {
	@Id
	@Column(name = "PRODUCT_CODE", nullable = false)
	private String productCode;
	@Column(name = "STORE_NO")
	private String storeNo;
	@Column(name = "PRODUCT_PRICE", precision= 19 ,scale = 2)
	private BigDecimal productPrice;
	@Column(name = "STOCK_NUM")
	private Long stockNum;
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;
	@Column(name = "IN_SECKILL")
	private String inSeckill;
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public BigDecimal getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}
	public Long getStockNum() {
		return stockNum;
	}
	public void setStockNum(Long stockNum) {
		this.stockNum = stockNum;
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
	public String getInSeckill() {
		return inSeckill;
	}
	public void setInSeckill(String inSeckill) {
		this.inSeckill = inSeckill;
	}
	

}
