package com.xiaoke1256.orders.thirdpayplatform.dto;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;

public class OrderResp extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ThirdPayOrderDto order;

	public OrderResp() {
		super();
	}

	public OrderResp(ErrorCode errorCode) {
		super(errorCode);
	}

	public OrderResp(String code, String msg) {
		super(code, msg);
	}

	public OrderResp(ThirdPayOrderDto order) {
		super(ErrorCode.SUCCESS);
		this.order = order;
	}

	public ThirdPayOrderDto getOrder() {
		return order;
	}

	public void setOrder(ThirdPayOrderDto order) {
		this.order = order;
	}
	
	

}
