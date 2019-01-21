package com.xiaoke1256.orders.common;


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
	
}
