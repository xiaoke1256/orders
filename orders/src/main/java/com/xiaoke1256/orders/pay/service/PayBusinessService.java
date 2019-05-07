package com.xiaoke1256.orders.pay.service;

/**
 * 与支付有关的具体业务
 * 主要用于处理调用第三方支付前后相关的事务。
 * @author Administrator
 *
 */
public interface PayBusinessService {
	public void notice(String orderNo ,String payType ,String remark);
	
	public void cancel(String orderNo,String cancelType ,String remark);
}
