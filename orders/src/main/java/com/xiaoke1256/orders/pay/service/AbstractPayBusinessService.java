package com.xiaoke1256.orders.pay.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;

public abstract class AbstractPayBusinessService implements PayBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractPayBusinessService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Transactional
	protected void savePayment(String thirdOrderNo, String payerNo, String payeeNo, String payType, BigDecimal amt, String payOrderNo,
			String subOrderNo,String businessNo, String incident) {
		//查一下该订单号 ，是否已用过。
		PaymentTxn payment = this.getPaymentByThirdOrderNo(thirdOrderNo);
		if(payment!=null) {
			//TODO 有可能是黑客攻击事件，需要记录安全日志。
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The third order no has usered","重复使用已支付过的订单。");
		}
		
		
		//记录支付流水
		PaymentTxn paymentTxn = new PaymentTxn();
		paymentTxn.setPayType(payType);
		paymentTxn.setPayeeNo(payeeNo);
		paymentTxn.setPayerNo(payerNo);
		paymentTxn.setAmt(amt);
		paymentTxn.setPayOrderNo(payOrderNo);
		paymentTxn.setSubOrderNo(subOrderNo);
		paymentTxn.setBusinessNo(businessNo);
		paymentTxn.setThirdOrderNo(thirdOrderNo);
		paymentTxn.setReverseFlg("0");//未冲正
		paymentTxn.setIncident(incident);
		paymentTxn.setInsertTime(new Timestamp(System.currentTimeMillis()));
		paymentTxn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(paymentTxn);
	}
	
	@Transactional(readOnly=true)
	public PaymentTxn getPaymentByThirdOrderNo(String thirdOrderNo) {
		String ql = "from PaymentTxn where thirdOrderNo = :thirdOrderNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(ql).setParameter("thirdOrderNo", thirdOrderNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 冲正
	 * @param orgTxn
	 * @param reason
	 */
	protected void reverse(PaymentTxn orgTxn,String reason) {
		try {
			entityManager.refresh(orgTxn, LockModeType.WRITE);
			if(!"0".equals(orgTxn.getReverseFlg())) {
				//已经被冲正了。
				logger.warn("Already been reversed!payment_id is:"+orgTxn.getPaymentId());
				return;
			}
			orgTxn.setReverseFlg("1");
			entityManager.merge(orgTxn);
			
			//冲正记录
			PaymentTxn paymentTxn = new PaymentTxn();
			BeanUtils.copyProperties(paymentTxn, orgTxn);
			paymentTxn.setPaymentId(null);
			paymentTxn.setAmt(orgTxn.getAmt().negate());
			paymentTxn.setIncident("冲正记录:"+orgTxn.getIncident());
			paymentTxn.setRemark(reason);
			paymentTxn.setReverseFlg("0");
			paymentTxn.setInsertTime(new Timestamp(System.currentTimeMillis()));
			paymentTxn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			paymentTxn.setDealStatus(PaymentTxn.DEAL_STATUS_INIT);
			entityManager.persist(paymentTxn);
		}catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	protected String getReson(String cancelType) {
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
		return reason;
	}
	
	@Override
	public void cancel(String orderNo, String cancelType, String remark) {
		String reason = getReson(cancelType);
		PaymentTxn orgTxn = null;
		if(StringUtils.isNotEmpty(orderNo)) {
			orgTxn = getPaymentByThirdOrderNo(orderNo);
		}else if(StringUtils.isNotEmpty(remark)) {
			orgTxn = getPaymentByBusiness(remark);
		}
		if(orgTxn==null) {
			logger.warn("Have not found the order by the orderNo.Maybe the order never input in our system.");
			return;//订单不存在就视为已经取消了。
		}
		cancel(orgTxn, reason);
		reverse(orgTxn,reason);
	}

	/**
	 * 处理具体业务的取消。
	 * @param orgTxn
	 * @param reason
	 */
	protected abstract void cancel(PaymentTxn orgTxn, String reason);

	/**
	 * 根据业务来获取支付流水
	 * @param remark
	 * @return
	 */
	protected abstract PaymentTxn getPaymentByBusiness(String remark);
}
