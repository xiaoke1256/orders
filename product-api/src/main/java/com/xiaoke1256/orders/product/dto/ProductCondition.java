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

	private String storeNo;

	private String[] storeNos;
	
	private boolean needFullTypeName;

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

	public boolean isNeedFullTypeName() {
		return needFullTypeName;
	}

	public void setNeedFullTypeName(boolean needFullTypeName) {
		this.needFullTypeName = needFullTypeName;
	}


	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String[] getStoreNos() {
		return storeNos;
	}

	public void setStoreNos(String[] storeNos) {
		this.storeNos = storeNos;
	}
}
