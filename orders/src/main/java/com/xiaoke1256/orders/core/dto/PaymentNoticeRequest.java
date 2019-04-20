package com.xiaoke1256.orders.core.dto;

public class PaymentNoticeRequest {
	/**
	 * 校验码
	 */
	private String verifyCode;
	
	/**
	 * （第三方支付的）订单号
	 */
	private String orderNo;
	
	private String payOrderNo;
	
	private String payType;

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

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	
}
