package com.xiaoke1256.orders.common.exception;

import com.xiaoke1256.orders.common.RespCode;

/**
 * 业务异常
 * @author Administrator
 *
 */
public class BusinessException extends AppException {
	
	private String showMsg;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
		errorCode=RespCode.BUSSNESS_ERROR.getCode();
		errorMsg = RespCode.BUSSNESS_ERROR.getMsg();
	}

	public BusinessException(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}
	
	public BusinessException(String errorCode, String errorMsg,String showMsg) {
		super(errorCode, errorMsg);
		if(errorCode==null || ""==errorCode.trim())
			errorCode=RespCode.BUSSNESS_ERROR.getCode();
		this.showMsg = showMsg;
	}

	public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorCode=RespCode.BUSSNESS_ERROR.getCode();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		errorCode=RespCode.BUSSNESS_ERROR.getCode();
	}

	public BusinessException(String showMsg) {
		super(showMsg);
		errorCode=RespCode.BUSSNESS_ERROR.getCode();
		errorMsg = RespCode.BUSSNESS_ERROR.getMsg();
		this.showMsg = showMsg;
	}

	public BusinessException(Throwable cause) {
		super(cause);
		errorCode=RespCode.BUSSNESS_ERROR.getCode();
		errorMsg = RespCode.BUSSNESS_ERROR.getMsg();
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}
	
	

}
