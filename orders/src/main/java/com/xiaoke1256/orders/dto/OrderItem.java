package com.xiaoke1256.orders.dto;

public class OrderItem {

	private Long itemId;

	private Long payOrderId;
	
	private String productCode;

	private String optionCode;

	private Integer productNum;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getOptionCode() {
		return optionCode;
	}

	public void setOptionCode(String optionCode) {
		this.optionCode = optionCode;
	}

	public Integer getProductNum() {
		return productNum;
	}

	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}
	
	
}
