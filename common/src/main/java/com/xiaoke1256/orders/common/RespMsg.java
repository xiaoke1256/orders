package com.xiaoke1256.orders.common;

import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;

/**
 * Response message for restful request.
 * @author Administrator
 *
 */
public class RespMsg implements java.io.Serializable {
	/* constructions*/
	public RespMsg() {
		super();
	}
	
	public RespMsg(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	
	public RespMsg(RespCode errorCode) {
		super();
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
	}
	
	public RespMsg(RespCode errorCode, String msg) {
		super();
		this.code = errorCode.getCode();
		this.msg = msg;
	}

	public RespMsg(RespCode errorCode, String msg, Object data) {
		super();
		this.code = errorCode.getCode();
		this.msg = msg;
		this.data = data;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
	private Object data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static final RespMsg SUCCESS = new RespMsg(RespCode.SUCCESS);
	
	public RespMsg(Exception ex) {
		super();
		this.setCode(RespCode.OTHER_ERROR.getCode());
		this.setMsg(RespCode.OTHER_ERROR.getMsg());
	}
	
	public RespMsg(AppException ex) {
		super();
		this.setCode(ex.getErrorCode());
		if(ex instanceof BusinessException)
			this.setMsg(((BusinessException)ex).getShowMsg());
		else
			this.setMsg(ex.getErrorMsg());
	}
	
	public RespMsg(BusinessException ex) {
		super();
		this.setCode(ex.getErrorCode());
		this.setMsg(ex.getShowMsg());
	}
	
}
