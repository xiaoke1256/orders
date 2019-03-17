package com.xiaoke1256.orders.common.exception;

/**
 * Orders应用内的通用异常。
 * @author Administrator
 *
 */
public class AppException extends Exception {

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
		errorCode=ErrorCode.OTHER_ERROR.getCode();
	}

	public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorCode=ErrorCode.OTHER_ERROR.getCode();
	}

	public AppException(String message) {
		super(message);
		errorCode=ErrorCode.OTHER_ERROR.getCode();
	}

	public AppException(Throwable cause) {
		super(cause);
		errorCode=ErrorCode.OTHER_ERROR.getCode();
		errorMsg = ErrorCode.OTHER_ERROR.getMsg();
	}

	public AppException(String errorCode, String errorMsg) {
		super();
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
