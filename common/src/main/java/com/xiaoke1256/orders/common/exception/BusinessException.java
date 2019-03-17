package com.xiaoke1256.orders.common.exception;

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
		errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
		errorMsg = ErrorCode.BUSSNESS_ERROR.getMsg();
	}

	public BusinessException(String errorCode, String errorMsg) {
		super(errorCode, errorMsg);
	}
	
	public BusinessException(String errorCode, String errorMsg,String showMsg) {
		super(errorCode, errorMsg);
		if(errorCode==null || ""==errorCode.trim())
			errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
		this.showMsg = showMsg;
	}

	public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
	}

	public BusinessException(String message) {
		super(message);
		errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
	}

	public BusinessException(Throwable cause) {
		super(cause);
		errorCode=ErrorCode.BUSSNESS_ERROR.getCode();
		errorMsg = ErrorCode.BUSSNESS_ERROR.getMsg();
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}
	
	

}
