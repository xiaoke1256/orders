package com.xiaoke1256.orders.core.bo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table( name = "ORDER_ITEM")
public class OrderItem {
	@Id
	@Column(name = "ITEM_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long itemId;
	
	@ManyToOne
	@JoinColumn(name="ORDER_NO")
	private SubOrder subOrder;
	
	@Column(name = "PAY_ORDER_ID")
	private Long payOrderId;
	
	@Column(name = "PRODUCT_CODE")
	private String productCode;
	
	@Column(name = "OPTION_CODE")
	private String optionCode;
	
	@Column(name = "PRODUCT_PRICE")
	private BigDecimal productPrice;
	
	@Column(name = "PRODUCT_NUM")
	private Integer productNum;

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemId == null) ? 0 : itemId.hashCode());
		result = prime * result + ((payOrderId == null) ? 0 : payOrderId.hashCode());
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
		if (payOrderId == null) {
			if (other.payOrderId != null)
				return false;
		} else if (!payOrderId.equals(other.payOrderId))
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

	public BigDecimal getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(BigDecimal productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getProductNum() {
		return productNum;
	}

	public void setProductNum(Integer productNum) {
		this.productNum = productNum;
	}
	
	
}
