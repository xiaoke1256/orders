package com.xiaoke1256.orders.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * product with no cascade.
 * @author xiaoke_1256
 *
 */
public class SimpleProduct implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productCode;
	
	private String productName;
	
	private BigDecimal productPrice;
	
	private String fullProductTypeName;
	
	private String storeName;
	
	private String storeNo;
	
	private String inSeckill;
	
	private String productStatus;
	
	private String productIntro;
	
	private String brand;
	
	private Timestamp insertTime;
	
	private Timestamp updateTime;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public String getFullProductTypeName() {
		return fullProductTypeName;
	}

	public void setFullProductTypeName(String fullProductTypeName) {
		this.fullProductTypeName = fullProductTypeName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getInSeckill() {
		return inSeckill;
	}

	public void setInSeckill(String inSeckill) {
		this.inSeckill = inSeckill;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getProductIntro() {
		return productIntro;
	}

	public void setProductIntro(String productIntro) {
		this.productIntro = productIntro;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	
	
}
