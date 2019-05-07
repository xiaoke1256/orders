package com.xiaoke1256.orders.common.exception;

import com.xiaoke1256.orders.common.RespCode;

/**
 * Orders应用内的通用异常。
 * @author Administrator
 *
 */
public class AppException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String errorCode;
	protected String errorMsg;
	
	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
		errorCode=RespCode.OTHER_ERROR.getCode();
	}
	
	public AppException(RespCode errorCode) {
		super(errorCode.getMsg());
		this.errorCode=errorCode.getCode();
		this.errorMsg = errorCode.getMsg();
	}

	public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorCode=RespCode.OTHER_ERROR.getCode();
	}

	public AppException(String message) {
		super(message);
		errorCode=RespCode.OTHER_ERROR.getCode();
	}

	public AppException(Throwable cause) {
		super(cause);
		errorCode=RespCode.OTHER_ERROR.getCode();
		errorMsg = RespCode.OTHER_ERROR.getMsg();
	}

	public AppException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
