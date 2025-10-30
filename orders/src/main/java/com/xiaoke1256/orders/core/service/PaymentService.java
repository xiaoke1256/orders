package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.bo.SubOrder;
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
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Autowired
	private OrederService orederService;
	
	/**
	 * 处理支付(收到第三方平台的支付看反馈后)
	 * @param payOrderNo 支付单号
	 * @param thirdOrderNo 第三方支付平台的订单号
	 * @param payType 支付类型
	 */
	public void notice(String thirdOrderNo,String payType,String payOrderNo) {
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		
		entityManager.refresh(payOrder, LockModeType.PESSIMISTIC_WRITE);
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		
		//保存订单流水
		this.savePayment(thirdOrderNo, payOrder.getPayerNo(), "000000000000000000", payType, payOrder.getTotalAmt(), payOrderNo, null,null, "购买商品");
		
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
		//TODO 推送mq，通知其他系统。
	}

	@Override
	protected PaymentTxn getPaymentByBusiness(String payOrderNo) {
		String ql = "from PaymentTxn where payOrderNo = :payOrderNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(ql).setParameter("payOrderNo", payOrderNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	public void savePayment(ThirdPayOrderDto orderInfo) {
		//TODO 检查token
		if (StringUtils.isBlank(orderInfo.getOrderType())) {
			// 默认为消费
			orderInfo.setOrderType(ThirdPayOrderDto.ORDER_TYPE_CONSUME);
		}
		PayOrder payOrder = orederService.getPayOrder(orderInfo.getMerchantOrderNo());
		if(payOrder==null){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order not exist","该订单不存在。");
		}
		String ql = "select count(t) from PaymentTxn t where payOrderNo = :payOrderNo and thirdOrderNo is null";
		Integer count = (Integer)entityManager.createQuery(ql).setParameter("payOrderNo", payOrder.getPayerNo()).getSingleResult();
		if(count>0){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}

		PaymentTxn payment = new PaymentTxn();
		payment.setPayerNo(orderInfo.getPayerNo());
		payment.setPayeeNo(orderInfo.getPayeeNo());
		payment.setPayType(orderInfo.getPayType());
		payment.setPayType(orderInfo.getOrderType());
		payment.setPayOrderNo(orderInfo.getMerchantOrderNo());
		payment.setAmt(orderInfo.getAmt());
		payment.setBusinessNo(orderInfo.getMerchantOrderNo());
		payment.setIncident(orderInfo.getIncident());
		payment.setRemark(orderInfo.getRemark());
		payment.setInsertTime(new Timestamp(System.currentTimeMillis()));
		payment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		payment.setDealStatus(PaymentTxn.DEAL_STATUS_INIT);
		entityManager.persist(payment);

		//TODO 发延迟消息，清理未支付的订单
	}

}
