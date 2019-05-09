package com.xiaoke1256.orders.pay.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayBusinessConfig {
	@Autowired
	private Map<String,PayBusinessService> payBusinessServices; 
	
	/**
	 * 从订单类型对应的业务。
	 */
	private static final Map<String,String> ORDER_TYPE_TO_BISINESS = new HashMap<>();
	static {
		ORDER_TYPE_TO_BISINESS.put("01", "paymentService");//01是消费
		ORDER_TYPE_TO_BISINESS.put("03", "makeMoneyService");//03与平台方结算
	}
	
	public PayBusinessService getPayBusinessService(String orderType) {
		return payBusinessServices.get(ORDER_TYPE_TO_BISINESS.get(orderType));
	}
}
