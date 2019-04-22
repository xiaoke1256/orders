package com.xiaoke1256.orders.core.dto;

public class PaymentCancelRequest {
	/**
	 * 取消类型：超时
	 */
	public static String CANCEL_TYPE_EXPIRED = "1";
	
	/**
	 * 取消类型：远程调用异常
	 */
	public static String CANCEL_TYPE_REMOTE_INVOK = "2";
	/**
	 * 取消类型：其他失败
	 */
	public static String CANCEL_TYPE_OTHER_FAIL = "9";
	
	private String orederNo;
	private String remark;
	private String cancelType;

	public PaymentCancelRequest() {
		super();
	}
	
	public PaymentCancelRequest(String orederNo, String remark,String cancelType) {
		super();
		this.orederNo = orederNo;
		this.remark = remark;
		this.cancelType = cancelType;
	}
	
	public String getOrederNo() {
		return orederNo;
	}
	public void setOrederNo(String orederNo) {
		this.orederNo = orederNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCancelType() {
		return cancelType;
	}
	public void setCancelType(String cancelType) {
		this.cancelType = cancelType;
	}

	
	
}
