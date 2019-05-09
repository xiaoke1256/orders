package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.bo.SettleItemOrder;
import com.xiaoke1256.orders.core.bo.SettleStatemt;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.pay.service.AbstractPayBusinessService;
import com.xiaoke1256.orders.pay.service.PayBusinessService;

/**
 * 处理打款业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class MakeMoneyService extends AbstractPayBusinessService implements PayBusinessService {
	
	private static final Logger logger = LoggerFactory.getLogger(MakeMoneyService.class);

	@Autowired
	private SettleService settleService;
	
	@Autowired
	private OrederService orederService;
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager;
	
	@Override
	public void notice(String orderNo, String payType, String remark) {
		String settleNo = remark;
		SettleStatemt settle = settleService.getSettleStatemtByNo(settleNo);
		entityManager.refresh(settle, LockModeType.PESSIMISTIC_WRITE);
		//检查月结单的状态
		if(SettleStatemt.STATUS_AWAIT_MAKE_MONEY.equals(settle.getStatus())) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The settlestatemt has payed","已结清.");
		}
		//保存支付流水
		this.savePayment(orderNo, "000000000000000000", settle.getStoreNo(), payType, settle.getPendingPayment(), null, null,settleNo, "结算款");
		//修改月结单的状态,包括待付款，已付款
		settle.setStatus(SettleStatemt.STATUS_HAS_MADE_MONEY);
		settle.setAlreadyPaid(settle.getPendingPayment());
		settle.setPendingPayment(BigDecimal.ZERO);
		settle.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Hibernate.initialize(settle.getSettleItemOrders());
		entityManager.merge(settle);
		//修改订单状态
		for(SettleItemOrder item : settle.getSettleItemOrders()) {
			SubOrder subOrder = orederService.getSubOrder(item.getOrderNo());
			subOrder.setStatus(SubOrder.ORDER_STATUS_FINISH);
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(subOrder);
		}
	}

	@Override
	public void cancel(String orderNo, String cancelType, String remark) {
		String reason = getReson(cancelType);
		String settleNo = remark;
		PaymentTxn orgTxn = null;
		if(StringUtils.isNotEmpty(orderNo)) {
			orgTxn = getPaymentByThirdOrderNo(orderNo);
		}else if(StringUtils.isNotEmpty(settleNo)) {
			orgTxn = getPaymentByBusiness(settleNo);
		}
		if(orgTxn==null) {
			logger.warn("Have not found the order by the orderNo.Maybe the order never input in our system.");
			return;//订单不存在就视为已经取消了。
		}
		cancel(orgTxn, reason);
		reverse(orgTxn,reason);
	}
	
	private void cancel(PaymentTxn orgTxn, String reason) {
		String settleNo = orgTxn.getBusinessNo();
		SettleStatemt settle = settleService.getSettleStatemtByNo(settleNo);
		//月结单的状态还原,包括待付款，已付款
		settle.setStatus(SettleStatemt.STATUS_AWAIT_MAKE_MONEY);
		settle.setPendingPayment(settle.getAlreadyPaid());
		settle.setAlreadyPaid(BigDecimal.ZERO);
		settle.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Hibernate.initialize(settle.getSettleItemOrders());
		entityManager.merge(settle);
		//订单状态还原
		for(SettleItemOrder item : settle.getSettleItemOrders()) {
			SubOrder subOrder = orederService.getSubOrder(item.getOrderNo());
			subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_MAKE_MONEY);
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(subOrder);
		}
	}

	protected PaymentTxn getPaymentByBusiness(String settleNo){
		String ql = "from PaymentTxn where businessNo = :businessNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(ql).setParameter("businessNo", settleNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	}

}
