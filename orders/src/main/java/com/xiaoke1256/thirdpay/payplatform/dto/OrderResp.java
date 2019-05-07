package com.xiaoke1256.thirdpay.payplatform.dto;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;

public class OrderResp extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ThirdPayOrderDto order;

	public OrderResp() {
		super();
	}

	public OrderResp(RespCode errorCode) {
		super(errorCode);
	}

	public OrderResp(String code, String msg) {
		super(code, msg);
	}

	public OrderResp(ThirdPayOrderDto order) {
		super(RespCode.SUCCESS);
		this.order = order;
	}
	
	public OrderResp(Exception ex) {
		super();
		this.setCode(RespCode.OTHER_ERROR.getCode());
		this.setMsg(RespCode.OTHER_ERROR.getMsg());
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
