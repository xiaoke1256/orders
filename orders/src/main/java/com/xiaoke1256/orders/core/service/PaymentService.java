package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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

	private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Autowired
	private OrederService orederService;

	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	
	/**
	 * 处理支付(收到第三方平台的支付看反馈后)
	 * @deprecated
	 * @param payOrderNo 支付单号
	 * @param thirdOrderNo 第三方支付平台的订单号
	 * @param payType 支付类型
	 */
	public void notice(String thirdOrderNo,String payType,String payOrderNo) {
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		
		entityManager.refresh(payOrder, LockModeType.PESSIMISTIC_WRITE);
		if(!PayOrder.ORDER_STATUS_INIT.equals(payOrder.getStatus())) {
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
	 * @deprecated
	 * 取消支付
	 * @param orgTxn
	 * @param reason
	 */
	public void cancel(PaymentTxn orgTxn,String reason) {
		String payOrderNo = orgTxn.getPayOrderNo();
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		payOrder.setStatus(PayOrder.ORDER_STATUS_INIT);
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(payOrder);
		for(SubOrder subOrder:payOrder.getSubOrders()) {
			subOrder.setStatus(SubOrder.ORDER_STATUS_PAYING);
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(subOrder);
		}
		//TODO 推送mq，通知其他系统。
	}

	/**
	 * @deprecated
	 * @param payOrderNo
	 * @return
	 */
	@Override
	protected PaymentTxn getPaymentByBusiness(String payOrderNo) {
		String ql = "from PaymentTxn where payOrderNo = :payOrderNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(ql).setParameter("payOrderNo", payOrderNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

	public PaymentTxn savePayment(String payOrderNo,String payType, String remark) {
		//TODO 检查token，防止重复提交
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		if(payOrder==null){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order not exist","该订单不存在。");
		}
		String ql = "select count(t) from PaymentTxn t where payOrderNo = :payOrderNo and thirdOrderNo is not null";
		Number count = (Number)entityManager.createQuery(ql).setParameter("payOrderNo", payOrder.getPayOrderNo()).getSingleResult();
		if( count!=null && count.intValue()>0){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		if(PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已支付，请等待订单处理结果。");
		}
		if(!PayOrder.ORDER_STATUS_INIT.equals(payOrder.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}

		PaymentTxn payment = new PaymentTxn();
		payment.setPayerNo(payOrder.getPayerNo());
		payment.setPayeeNo("000000000000000000");
		payment.setPayType(payType);
		//payment.setOrderType(orderType);
		payment.setPayOrderNo(payOrderNo);
		payment.setAmt(payOrder.getTotalAmt());
		payment.setBusinessNo(payOrder.getPayOrderNo());
		payment.setIncident(null);
		payment.setRemark(remark);
		payment.setInsertTime(new Timestamp(System.currentTimeMillis()));
		payment.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		payment.setDealStatus(PaymentTxn.DEAL_STATUS_INIT);
		entityManager.persist(payment);

		//发延迟消息，清理未支付的订单
		Message<String> strMessage = MessageBuilder.withPayload(JSON.toJSONString(payment) )
				.setHeader(RocketMQHeaders.MESSAGE_ID, payment.getPaymentId()).build();
		SendResult sendResult = rocketMQTemplate.syncSend("clear_expired_order", strMessage,2000,9);//9对应的常量是 5m;5分钟后未支付则清理
		LOG.info("同步发送字符串{}, 发送结果{}", payment, sendResult);
		return payment;
	}

	/**
	 * 第三方支付机构已受理，等待支付结果
	 * @param
	 */
	public void payed(String thirdPayOrderNo,Long paymentId) {
		PaymentTxn payment = entityManager.find(PaymentTxn.class, paymentId, LockModeType.PESSIMISTIC_WRITE);
		if(PaymentTxn.PAY_STATUS_SUCCESS.equals( payment.getPayStatus())||PaymentTxn.PAY_STATUS_FAIL.equals( payment.getPayStatus())){
			LOG.info("订单已处理，忽略");
			return;
		}
		Query updateQuery = entityManager.createQuery("update PaymentTxn t set t.payStatus = :payStatus , t.thirdOrderNo = :thirdOrderNo ,t.updateTime = :updateTime" +
				" where t.paymentId = :paymentId and t.payStatus='" + PaymentTxn.PAY_STATUS_INIT + "'");
		int resultCount = updateQuery.setParameter("payStatus", PaymentTxn.PAY_STATUS_PAYING)
				.setParameter("thirdOrderNo", thirdPayOrderNo)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("paymentId", paymentId).executeUpdate();
		if(resultCount==0){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		//处理payOrder状态
		PayOrder payOrder = orederService.getPayOrder(payment.getPayOrderNo());
		Query updatePayOrderQuery = entityManager.createQuery("update PayOrder t set t.status = :status ,t.updateTime = :updateTime" +
				" where t.payOrderNo=:payOrderNo and t.status = '"+PayOrder.ORDER_STATUS_INIT+"'");
		int payOrderResultCount = updatePayOrderQuery.setParameter("status", PayOrder.ORDER_STATUS_PAYING)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("payOrderNo", payment.getPayOrderNo()).executeUpdate();
		if(payOrderResultCount==0){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
	}

	/**
	 * 支付成功
	 * @param payOrderNo
	 * @param paymentId
	 */
	public void notifyPaySuccess(String payOrderNo,String thirdPayOrderNo,Long paymentId) {
		//支付记录改掉
		LOG.info("notify 支付成功 ,payOrderNo:"+payOrderNo+",thirdPayOrderNo:"+thirdPayOrderNo+",paymentId:"+paymentId);
		PaymentTxn payment = entityManager.find(PaymentTxn.class, paymentId, LockModeType.PESSIMISTIC_WRITE);
		/*
		 * 状态只能是INIT,PAYING,EXPIRED
		 * PAYING 是正常流程
		 * INIT 是因为回调页面没有调用成功
		 * EXPIRED 原因是支付平台处理时间太长
		 */
		if(!Arrays.asList(PaymentTxn.PAY_STATUS_INIT,PaymentTxn.PAY_STATUS_PAYING,PaymentTxn.PAY_STATUS_EXPIRED).contains(payment.getPayStatus())  ){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经支付过了。");
		}
		//更改支付单
		Query updateQuery = entityManager.createQuery("update PaymentTxn t set t.payStatus = :payStatus ," +
				"t.thirdOrderNo= :thirdOrderNo ,t.updateTime = :updateTime ," +
				"t.resultCode= :resultCode ,t.resultMsg = :resultMsg" +
				" where t.paymentId = :paymentId ");
		updateQuery.setParameter("payStatus", PaymentTxn.PAY_STATUS_SUCCESS)
				.setParameter("thirdOrderNo", thirdPayOrderNo)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("resultCode", RespCode.SUCCESS.getCode())
				.setParameter("resultMsg", RespCode.SUCCESS.getMsg())
				.setParameter("paymentId", paymentId).executeUpdate();
		//处理payOrder状态
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		Query updatePayOrderQuery = entityManager.createQuery("update PayOrder t set t.status = :status ,t.updateTime = :updateTime" +
				" where t.payOrderNo=:payOrderNo");
		int payOrderResultCount = updatePayOrderQuery.setParameter("status", PayOrder.ORDER_STATUS_PAYED)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("payOrderNo", payOrderNo).executeUpdate();
		if(payOrderResultCount==0) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(), "The order is not exists", "该支付单不存在。");
		}
		//处理子订单状态
		Query updateSubOrderQuery = entityManager.createQuery("update SubOrder t set t.status = :status ,t.updateTime = :updateTime" +
				" where t.payOrder.payOrderId=:payOrderId ");
		int subOrderResultCount = updateSubOrderQuery.setParameter("status", SubOrder.ORDER_STATUS_AWAIT_ACCEPT)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("payOrderId", payOrder.getPayOrderId()).executeUpdate();
		if(subOrderResultCount==0) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(), "The order is not exists", "子订单不存在。");
		}
		LOG.info("notify success");
	}

	/**
	 * 支付失败
	 * @param payOrderNo
	 * @param paymentId
	 */
	public void notifyPayFail(String payOrderNo,String thirdPayOrderNo,Long paymentId,String resultCode,String msg) {
		LOG.info("notify 支付失败 ,payOrderNo:"+payOrderNo+",thirdPayOrderNo:"+thirdPayOrderNo+",paymentId:"+paymentId);
		PaymentTxn payment = entityManager.find(PaymentTxn.class, paymentId, LockModeType.PESSIMISTIC_WRITE);

		if(Arrays.asList(PaymentTxn.PAY_STATUS_SUCCESS,PaymentTxn.PAY_STATUS_FAIL).contains(payment.getPayStatus())  ){
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order has payed","该订单已经处理过了。");
		}
		//TODO resultCode 和 msg 要写到 payment中
		//更改支付单
		Query updateQuery = entityManager.createQuery("update PaymentTxn t set t.payStatus = :payStatus ," +
				"t.thirdOrderNo= :thirdOrderNo ,t.updateTime = :updateTime ," +
				"t.resultCode= :resultCode ,t.resultMsg = :resultMsg" +
				" where t.paymentId = :paymentId ");
		updateQuery.setParameter("payStatus", PaymentTxn.PAY_STATUS_FAIL)
				.setParameter("thirdOrderNo", thirdPayOrderNo)
				.setParameter("updateTime", new Timestamp(System.currentTimeMillis()))
				.setParameter("resultCode", resultCode)
				.setParameter("resultMsg", msg)
				.setParameter("paymentId", paymentId).executeUpdate();
		LOG.info("notify success");
	}


}
