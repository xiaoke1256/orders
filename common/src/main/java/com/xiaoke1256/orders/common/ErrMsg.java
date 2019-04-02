package com.xiaoke1256.orders.common;

import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.ErrorCode;

public class ErrMsg extends RespMsg{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrMsg() {
		super();
	}
	
	public ErrMsg(String code, String msg) {
		super(code, msg);
	}
	
	public ErrMsg(ErrorCode errorCode) {
		super(errorCode);
	}

	public ErrMsg(Exception ex) {
		super();
		this.setCode(ErrorCode.OTHER_ERROR.getCode());
		this.setMsg(ex.getMessage());
	}
	
	public ErrMsg(AppException ex) {
		super();
		this.setCode(ex.getErrorCode());
		if(ex instanceof BusinessException)
			this.setMsg(((BusinessException)ex).getShowMsg());
		else
			this.setMsg(ex.getErrorMsg());
	}
	
	public ErrMsg(BusinessException ex) {
		super();
		this.setCode(ex.getErrorCode());
		this.setMsg(ex.getShowMsg());
	}
	
}
