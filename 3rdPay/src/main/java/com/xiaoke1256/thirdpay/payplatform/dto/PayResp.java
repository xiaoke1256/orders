package com.xiaoke1256.thirdpay.payplatform.dto;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;

public class PayResp extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public PayResp(RespCode errorCode, String verifyCode, String orderNo) {
		super(errorCode);
		this.verifyCode = verifyCode;
		this.orderNo = orderNo;
	}

	/**
	 * 校验码
	 */
	public String verifyCode;
	
	/**
	 * 订单号
	 */
	public String orderNo;

}
