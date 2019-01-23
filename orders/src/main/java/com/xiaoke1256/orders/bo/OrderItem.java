package com.xiaoke1256.orders.bo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class OrderItem {
	@Id
	@Column(name = "ITEM_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;
	
	@ManyToOne
	@JoinColumn(name="SUB_ORDER_ID")
	private SubOrder subOrder;
	
	@Column(name = "PAY_ORDER_NO")
	private String payOrderNo;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "OPTION_CODE")
	private String optionCode;
	
	@Column(name = "PRODUCT_NUM")
	private Integer productNum;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((payOrderNo == null) ? 0 : payOrderNo.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((subOrder == null) ? 0 : subOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderItem other = (OrderItem) obj;
		if (itemId == null) {
			if (other.itemId != null)
				return false;
		} else if (!itemId.equals(other.itemId))
			return false;
		if (payOrderNo == null) {
			if (other.payOrderNo != null)
				return false;
		} else if (!payOrderNo.equals(other.payOrderNo))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (subOrder == null) {
			if (other.subOrder != null)
				return false;
		} else if (!subOrder.equals(other.subOrder))
			return false;
		return true;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public SubOrder getSubOrder() {
		return subOrder;
	}

	public void setSubOrder(SubOrder subOrder) {
		this.subOrder = subOrder;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
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
