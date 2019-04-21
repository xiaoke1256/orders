package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.exception.AppException;
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
	
	@Transactional(readOnly=true)
	public PaymentTxn getPaymentByPayOrderNo(String payOrderNo) {
		String ql = "from PaymentTxn where payOrderNo = :payOrderNo";
		PaymentTxn txn = (PaymentTxn)entityManager.createQuery(ql).setParameter("payOrderNo", payOrderNo).getSingleResult();
		return txn;
	}
	
	@Transactional(readOnly=true)
	public PaymentTxn getPaymentByThirdOrderNo(String thirdOrderNo) {
		String ql = "from PaymentTxn where thirdOrderNo = :thirdOrderNo";
		PaymentTxn txn = (PaymentTxn)entityManager.createQuery(ql).setParameter("thirdOrderNo", thirdOrderNo).getSingleResult();
		return txn;
	}
	
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
	
	/**
	 * 冲正
	 * @param orgTxn
	 * @param reason
	 */
	public void reverse(PaymentTxn orgTxn,String reason) {
		try {
			entityManager.refresh(orgTxn, LockModeType.WRITE);
			if(!"0".equals(orgTxn.getReverseFlg())) {
				throw new AppException(ErrorCode.CONCURRENCY_ERROR);
			}
			orgTxn.setReverseFlg("1");
			entityManager.merge(orgTxn);
			
			//冲正记录
			PaymentTxn paymentTxn = new PaymentTxn();
			BeanUtils.copyProperties(paymentTxn, orgTxn);
			paymentTxn.setAmt(orgTxn.getAmt().negate());
			paymentTxn.setIncident("冲正记录:"+orgTxn.getIncident());
			paymentTxn.setRemark(reason);
			paymentTxn.setInsertTime(new Timestamp(System.currentTimeMillis()));
			paymentTxn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			paymentTxn.setDealStatus(PaymentTxn.DEAL_STATUS_INIT);
			entityManager.persist(paymentTxn);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
