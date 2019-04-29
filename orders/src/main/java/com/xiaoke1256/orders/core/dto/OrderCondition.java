package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.page.BaseCondition;

public class OrderCondition extends BaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**商铺号*/
	private String storeNo;
	
	/**状态*/
	private String status;
	
	/**订单号*/
	private String orderNo;

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	

}
