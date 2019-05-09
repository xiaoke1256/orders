package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.pay.service.AbstractPayBusinessService;
import com.xiaoke1256.orders.pay.service.PayBusinessService;

/**
 * 与支付有关的业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class PaymentService extends AbstractPayBusinessService implements PayBusinessService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Autowired
	private OrederService orederService;
	
	@Transactional(readOnly=true)
	public PaymentTxn getPaymentByPayOrderNo(String payOrderNo) {
		String ql = "from PaymentTxn where payOrderNo = :payOrderNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(ql).setParameter("payOrderNo", payOrderNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 处理支付(收到第三方平台的支付看反馈后)
	 * @param payOrderNo 支付单号
	 * @param thirdOrderNo 第三方支付平台的订单号
	 * @param payType 支付类型
	 * @param verifyInfo 支付校验信息
	 */
	public void notice(String thirdOrderNo,String payType,String payOrderNo) {
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		
		entityManager.refresh(payOrder, LockModeType.PESSIMISTIC_WRITE);
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		
		//保存订单流水
		this.savePayment(thirdOrderNo, payOrder.getPayerNo(), "000000000000000000", payType, payOrder.getTotalAmt(), payOrderNo, null, "购买商品");
		
		//修改订单状态
		payOrder.setStatus(PayOrder.ORDER_STATUS_PAYED);
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(payOrder);
		for(SubOrder subOrder:payOrder.getSubOrders()) {
			subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_ACCEPT);
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(subOrder);
		}
		//TODO 推送mq，通知其他系统。
	}
	
	/**
	 * 取消支付
	 * @param orgTxn
	 * @param reason
	 */
	public void cancel(PaymentTxn orgTxn,String reason) {
		String payOrderNo = orgTxn.getPayOrderNo();
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		payOrder.setStatus(PayOrder.ORDER_STATUS_PAYING);
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(payOrder);
		for(SubOrder subOrder:payOrder.getSubOrders()) {
			subOrder.setStatus(SubOrder.ORDER_STATUS_PAYING);
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(subOrder);
		}
		reverse(orgTxn,reason);
		//TODO 推送mq，通知其他系统。
	}
	
	/**
	 * 取消
	 */
	@Override
	public void cancel(String thirdOrderNo, String cancelType, String payOrderNo) {
		String reason="";
		if(PaymentCancelRequest.CANCEL_TYPE_EXPIRED.equals(cancelType)) {
			reason="超时未反馈。";
		}else if(PaymentCancelRequest.CANCEL_TYPE_REMOTE_INVOK.equals(cancelType)) {
			reason="远程调用异常。";
		}else if(PaymentCancelRequest.CANCEL_TYPE_OTHER_FAIL.equals(cancelType)) {
			reason="其他异常。";
		}
		if(StringUtils.isEmpty(reason)) {
			throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"未提供足够的参数");
		}
		

		PaymentTxn orgTxn = null;
		if(StringUtils.isNotEmpty(thirdOrderNo)) {
			orgTxn = getPaymentByThirdOrderNo(thirdOrderNo);
		}else if(StringUtils.isNotEmpty(payOrderNo)) {
			orgTxn = getPaymentByPayOrderNo(payOrderNo);
		}
		if(orgTxn==null) {
			logger.warn("Have not found the order by the orderNo.Maybe the order never input in our system.");
			return;//订单不存在就视为已经取消了。
		}
		cancel(orgTxn, reason);
	}

}
