package com.xiaoke1256.orders.bo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "PRODUCT")
public class OStorage {
	@Id
	@Column(name = "STORAGE_ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long storageId;
	@Column(name = "PRODUCT_CODE", nullable = false)
	private String productCode;
	@Column(name = "OPTION_CODE")
	private String optionCode;
	@Column(name = "STOCK_NUM")
	private Long stockNum;
	@Column(name = "INSERT_TIME")
	private Timestamp insertTime;
	@Column(name = "UPDATE_TIME")
	private Timestamp updateTime;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((optionCode == null) ? 0 : optionCode.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((storageId == null) ? 0 : storageId.hashCode());
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
		OStorage other = (OStorage) obj;
		if (optionCode == null) {
			if (other.optionCode != null)
				return false;
		} else if (!optionCode.equals(other.optionCode))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (storageId == null) {
			if (other.storageId != null)
				return false;
		} else if (!storageId.equals(other.storageId))
			return false;
		return true;
	}
	public Long getStorageId() {
		return storageId;
	}
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
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
	
	
	

}
