package com.xiaoke1256.thirdpay.payplatform.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ThirdPayOrderDto implements java.io.Serializable {
	
	/** 订单类型：消费*/
	public static final String ORDER_TYPE_CONSUME = "01";
	/** 订单类型：退货*/
	public static final String ORDER_TYPE_RETURN = "02";
	/** 订单类型：与平台方结算*/
	public static final String ORDER_TYPE_SETTLE = "03";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String payerNo;
	private String payeeNo;
	private String orderType;
	private String payType;
	private BigDecimal amt;
	private String merchantNo;
	private String merchantOrderNo;
	private String incident;
	private String remark;
	private Timestamp insertTime;
}
