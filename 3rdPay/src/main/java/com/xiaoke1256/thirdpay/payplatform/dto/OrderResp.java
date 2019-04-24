package com.xiaoke1256.thirdpay.payplatform.dto;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
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
	
	public OrderResp(Exception ex) {
		super();
		this.setCode(ErrorCode.OTHER_ERROR.getCode());
		this.setMsg(ErrorCode.OTHER_ERROR.getMsg());
	}
	
	public OrderResp(AppException ex) {
		super();
		this.setCode(ex.getErrorCode());
		if(ex instanceof BusinessException)
			this.setMsg(((BusinessException)ex).getShowMsg());
		else
			this.setMsg(ex.getErrorMsg());
	}
	
	public OrderResp(BusinessException ex) {
		super();
		this.setCode(ex.getErrorCode());
		this.setMsg(ex.getShowMsg());
	}

	public ThirdPayOrderDto getOrder() {
		return order;
	}

	public void setOrder(ThirdPayOrderDto order) {
		this.order = order;
	}
	
	

}
