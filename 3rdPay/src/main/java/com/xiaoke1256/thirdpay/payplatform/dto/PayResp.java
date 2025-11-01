package com.xiaoke1256.thirdpay.payplatform.dto;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;

public class PayResp extends RespMsg {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public PayResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PayResp(RespCode errorCode) {
		super(errorCode);
	}

	public PayResp(RespCode errorCode, String msg) {
		super(errorCode, msg);
	}

	public PayResp(RespCode errorCode, String verifyCode, String orderNo) {
		super(errorCode);
		this.verifyCode = verifyCode;
		this.orderNo = orderNo;
	}

	public PayResp(AppException ex) {
		super(ex);
	}

	public PayResp(BusinessException ex) {
		super(ex);
	}

	public PayResp(Exception ex) {
		super(ex);
	}



	/**
	 * 校验码
	 */
	private String verifyCode;
	
	/**
	 * 订单号
	 */
	private String orderNo;



	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


}
