package com.xiaoke1256.orders.common;

/**
 * Response message for restful request.
 * @author Administrator
 *
 */
public class RespMsg implements java.io.Serializable {
	public RespMsg() {
		super();
	}
	public RespMsg(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
	public RespMsg(String code, String msg,Object respObj) {
		super();
		this.respObj = respObj;
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object respObj;
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
	public Object getRespObj() {
		return respObj;
	}
	public void setRespObj(Object respObj) {
		this.respObj = respObj;
	}
	
	
	
}
