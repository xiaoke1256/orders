package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
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
	 * @param thirdOrderNo 第三方支付平台的订单号
	 * @param payType 支付类型
	 * @param verifyInfo 支付校验信息
	 */
	public void pay(String payOrderNo,String thirdOrderNo,String payType) {
		PayOrder payOrder = entityManager.find(PayOrder.class, payOrderNo);
		
		entityManager.refresh(payOrder, LockModeType.WRITE);
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(ErrorCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		
		//TODO 查一下该订单号 ，是否已用过。
		
		
		//记录支付流水
		PaymentTxn paymentTxn = new PaymentTxn();
		paymentTxn.setPayType(payType);
		paymentTxn.setPayeeNo("000000000000000000");
		paymentTxn.setPayerNo(payOrder.getPayerNo());
		paymentTxn.setAmt(payOrder.getTotalAmt());
		paymentTxn.setPayOrderNo(payOrder.getPayOrderNo());
		paymentTxn.setThirdOrderNo(thirdOrderNo);
		paymentTxn.setIncident("购买商品");
		paymentTxn.setInsertTime(new Timestamp(System.currentTimeMillis()));
		paymentTxn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(paymentTxn);
		
		//修改订单状态
		payOrder.setStatus(PayOrder.ORDER_STATUS_PAYED);
		entityManager.merge(payOrder);
		for(SubOrder subOrder:payOrder.getSubOrders()) {
			subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_ACCEPT);
			entityManager.merge(subOrder);
		}
		//TODO 推送mq，通知其他系统。
	}
}
