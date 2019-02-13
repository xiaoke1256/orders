package com.xiaoke1256.orders.product.dto;

import com.xiaoke1256.orders.common.page.BaseCondition;

public class ProductCondition extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String productCode;
	
	private String productName;
	
	private String productTypeName;

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

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}
	
	

}
