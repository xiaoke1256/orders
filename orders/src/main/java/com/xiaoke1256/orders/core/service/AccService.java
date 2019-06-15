package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.hibernate.LockMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.HouseholdAccTxn;
import com.xiaoke1256.orders.core.bo.PaymentTxn;

/**
 * 与财务记账有关的Service
 * @author Administrator
 *
 */
@Service
@Transactional
public class AccService {
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	/**
	 * 计分户账
	 */
	public void saveHousehold(List<PaymentTxn> txnList) {
		for(PaymentTxn txn:txnList) {
			entityManager.refresh(txn, LockModeType.PESSIMISTIC_WRITE);
			if(!PaymentTxn.DEAL_STATUS_INIT.equals(txn.getDealStatus())) {
				throw new RuntimeException("The txn is not in correct status.");
			}
			
			txn.getPayerNo();//TODO 查一查是否商户 或 平台
			//处理付款方
			HouseholdAccTxn srcHousehold = getCurrHouseholdByAccNo(txn.getPayerNo());
			BigDecimal balance = BigDecimal.ZERO;
			if(srcHousehold != null) {
				balance = srcHousehold.getCashBalance();
				srcHousehold.setIsCurrent("0");
				srcHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(srcHousehold);
			}
			balance = balance.subtract(txn.getAmt());
			HouseholdAccTxn payerHousehold = new HouseholdAccTxn();
			payerHousehold.setAccNo(txn.getPayerNo());
			payerHousehold.setAccFlg("-");
			payerHousehold.setPayOrderNo(txn.getPayOrderNo());
			payerHousehold.setSubOrderNo(txn.getSubOrderNo());
			payerHousehold.setIsCurrent("1");
			payerHousehold.setAmt(txn.getAmt());
			payerHousehold.setCashBalance(balance);
			payerHousehold.setInsertTime(new Timestamp(System.currentTimeMillis()));
			payerHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(payerHousehold);
			
			//处理收款方 //TODO 查一查是否商户 或 平台
			srcHousehold = getCurrHouseholdByAccNo(txn.getPayeeNo());
			balance = BigDecimal.ZERO;
			if(srcHousehold != null) {
				balance = srcHousehold.getCashBalance();
				srcHousehold.setIsCurrent("0");
				srcHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(srcHousehold);
			}
			balance = balance.add(txn.getAmt());
			HouseholdAccTxn payeeHousehold = new HouseholdAccTxn();
			payeeHousehold.setAccNo(txn.getPayeeNo());
			payeeHousehold.setAccFlg("+");
			payeeHousehold.setPayOrderNo(txn.getPayOrderNo());
			payeeHousehold.setSubOrderNo(txn.getSubOrderNo());
			payeeHousehold.setIsCurrent("1");
			payeeHousehold.setAmt(txn.getAmt());
			payeeHousehold.setCashBalance(balance);
			payeeHousehold.setInsertTime(new Timestamp(System.currentTimeMillis()));
			payeeHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.persist(payeeHousehold);
			
			txn.setDealStatus(PaymentTxn.DEAL_STATUS_HOUSEHOLD);
			txn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(txn);
		}
	}
	
	private HouseholdAccTxn getCurrHouseholdByAccNo(String accNo) {
		String hql = "from HouseholdAccTxn where accNo = :accNo and isCurrent = '1'";
		HouseholdAccTxn household = (HouseholdAccTxn) entityManager.createNativeQuery(hql).setParameter("accNo", accNo).getSingleResult();
		return household;
	}
	
	@Transactional(readOnly=true)
	public List<PaymentTxn> findByStatus(String dealStatus){
		String hql = "from PaymentTxn where dealStatus = :dealStatus order by insertTime";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(hql).setParameter("dealStatus", dealStatus).getResultList();
		return list;
	}
}
