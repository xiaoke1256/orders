package com.xiaoke1256.orders.product.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProductType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String typeId;
	private String typeName;
	private ProductType parentType;
	private String typeDesc;
	private Integer showOrder;
	private Timestamp insertTime;
	private Timestamp updateTime;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public ProductType getParentType() {
		return parentType;
	}
	public void setParentType(ProductType parentType) {
		this.parentType = parentType;
	}
	public String getTypeDesc() {
		return typeDesc;
	}
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	public Integer getShowOrder() {
		return showOrder;
	}
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
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
