package com.xiaoke1256.thirdpay.payplatform.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayRequest implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String payerNo;//付款方账号
	private String payeeNo;//收款方账号

	private String merchantPayerNo;
	private String merchantPayeeNo;
	private String orderType;
	private BigDecimal amt;
	private String merchantNo;
	private String merchantOrderNo;
	private String bussinessNo;
	private String incident;
	private String remark;
	private String payPwd;

}
