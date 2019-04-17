package com.xiaoke1256.orders.core.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;

/**
 * 与支付有关的业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class PaymentService {
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	/**
	 * 处理支付
	 * @param payOrderNo 支付单号
	 * @param verifyInfo 支付校验信息
	 */
	public void pay(String payOrderNo,String verifyInfo) {
		PayOrder payOrder = entityManager.find(PayOrder.class, payOrderNo);
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(ErrorCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		
		//TODO 调用第三方支付接口校验支付情况
		
		//记录支付流水
		//修改订单状态
		payOrder.setStatus(PayOrder.ORDER_STATUS_PAYED);
		entityManager.merge(payOrder);
		for(SubOrder subOrder:payOrder.getSubOrders()) {
			subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_ACCEPT);
			entityManager.merge(subOrder);
		}
	}
}
