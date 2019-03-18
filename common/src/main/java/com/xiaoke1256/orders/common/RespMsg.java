package com.xiaoke1256.orders.common;

import com.xiaoke1256.orders.common.exception.ErrorCode;

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
	
	public RespMsg(ErrorCode errorCode) {
		super();
		this.code = errorCode.getCode();
		this.msg = errorCode.getMsg();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;
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
	
	public static final RespMsg SUCCESS = new RespMsg(ErrorCode.SUCCESS);
	
}
