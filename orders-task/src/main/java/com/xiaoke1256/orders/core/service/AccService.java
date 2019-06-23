package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.HouseholdAccTxn;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.client.StoreQueryClient;
import com.xiaoke1256.orders.product.dto.Store;

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
	
	@Autowired
	private StoreQueryClient storeQueryService;
	
	/**
	 * 计分户账
	 */
	public void saveHousehold(List<PaymentTxn> txnList) {
		for(PaymentTxn txn:txnList) {
			entityManager.refresh(txn, LockModeType.PESSIMISTIC_WRITE);
			if(!PaymentTxn.DEAL_STATUS_INIT.equals(txn.getDealStatus())) {
				throw new RuntimeException("The txn is not in correct status.");
			}
			
			// 查一查是否商户 或 平台
			Store store = storeQueryService.getStore(txn.getPayerNo());
			if(store!=null || "000000000000000000".equals(txn.getPayerNo())) {
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
				payerHousehold.setPaymentId(txn.getPaymentId());
				payerHousehold.setAccFlg("-");
				payerHousehold.setPayOrderNo(txn.getPayOrderNo());
				payerHousehold.setSubOrderNo(txn.getSubOrderNo());
				payerHousehold.setIsCurrent("1");
				payerHousehold.setRemark(txn.getIncident());
				payerHousehold.setAmt(txn.getAmt());
				payerHousehold.setCashBalance(balance);
				payerHousehold.setInsertTime(new Timestamp(System.currentTimeMillis()));
				payerHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(payerHousehold);
			}
			
			store = storeQueryService.getStore(txn.getPayeeNo());
			if(store!=null || "000000000000000000".equals(txn.getPayeeNo())) {
				//处理收款方 //TODO 查一查是否商户 或 平台
				HouseholdAccTxn srcHousehold = getCurrHouseholdByAccNo(txn.getPayeeNo());
				BigDecimal balance = BigDecimal.ZERO;
				if(srcHousehold != null) {
					balance = srcHousehold.getCashBalance();
					srcHousehold.setIsCurrent("0");
					srcHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					entityManager.merge(srcHousehold);
				}
				balance = balance.add(txn.getAmt());
				HouseholdAccTxn payeeHousehold = new HouseholdAccTxn();
				payeeHousehold.setAccNo(txn.getPayeeNo());
				payeeHousehold.setPaymentId(txn.getPaymentId());
				payeeHousehold.setAccFlg("+");
				payeeHousehold.setPayOrderNo(txn.getPayOrderNo());
				payeeHousehold.setSubOrderNo(txn.getSubOrderNo());
				payeeHousehold.setIsCurrent("1");
				payeeHousehold.setRemark(txn.getIncident());
				payeeHousehold.setAmt(txn.getAmt());
				payeeHousehold.setCashBalance(balance);
				payeeHousehold.setInsertTime(new Timestamp(System.currentTimeMillis()));
				payeeHousehold.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.persist(payeeHousehold);
			}
			
			txn.setDealStatus(PaymentTxn.DEAL_STATUS_HOUSEHOLD);
			txn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(txn);
		}
	}
	
	private HouseholdAccTxn getCurrHouseholdByAccNo(String accNo) {
		String hql = "from HouseholdAccTxn where accNo = :accNo and isCurrent = '1'";
		@SuppressWarnings("unchecked")
		List<HouseholdAccTxn> list = entityManager.createQuery(hql).setParameter("accNo", accNo).getResultList();
		if(list!=null&&list.size()>0)
			return list.get(0);
		return null;
	}
	
	@Transactional(readOnly=true)
	public List<PaymentTxn> findByStatus(String dealStatus){
		String hql = "from PaymentTxn where dealStatus = :dealStatus order by insertTime";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(hql).setParameter("dealStatus", dealStatus).setMaxResults(1000).getResultList();
		return list;
	}
	
	public void doHousehold() {
		List<PaymentTxn> txns = findByStatus(PaymentTxn.DEAL_STATUS_INIT);
		if(txns==null || txns.isEmpty()) {
			return;
		}
		netting(txns);
		saveHousehold(txns);
	}
	
	/**
	 * 轧差（仅针对冲正）
	 */
	private void netting(List<PaymentTxn> txns) {
		List<PaymentTxn> toNettings = new ArrayList<PaymentTxn>();
		//从中找出冲正记录
		for(PaymentTxn txn:txns) {
			if(txn.getIncident() != null && txn.getIncident().startsWith("冲正记录:")) {
				toNettings.add(txn);
			}
		}
		
		for(PaymentTxn txn:toNettings) {
			PaymentTxn toBeRevers = null;
			//找到被冲正的记录
			if(StringUtils.isNotEmpty(txn.getSubOrderNo())) {
				//先按subOrderNo 查.
				toBeRevers = filterReversBySubOrderNo(txn,txns);
				
			}
			if(toBeRevers == null && StringUtils.isNotEmpty(txn.getPayOrderNo())) {
				// 再按payOrderNo 查.
				toBeRevers = filterReversByPayOrderNo(txn,txns);
			}
			
			if(toBeRevers!=null ) {
				if(!toBeRevers.getAmt().abs().equals(txn.getAmt().abs())) {
					throw new RuntimeException("Revers amt is not equals! txnId is :"+txn.getPaymentId()+" - "+toBeRevers.getPaymentId()
						+" ; amt: "+txn.getAmt()+" - "+ toBeRevers.getAmt());
				}
				txn.setDealStatus(PaymentTxn.DEAL_STATUS_HOUSEHOLD);
				txn.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(txn);
				toBeRevers.setDealStatus(PaymentTxn.DEAL_STATUS_HOUSEHOLD);
				toBeRevers.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				entityManager.merge(toBeRevers);
				txns.remove(txn);
				txns.remove(toBeRevers);
			} else {
				//此时还找不到就到历史记录里找
				toBeRevers = findReversBySubOrderNo(txn);
				if(toBeRevers == null)
					toBeRevers = findReversByPayOrderNo(txn);
				if(toBeRevers == null) {
					//此时没找到就按正常记录处理
					continue;
				}
				if(!toBeRevers.getAmt().abs().equals(txn.getAmt().abs())) {
					throw new RuntimeException("Revers amt is not equals! txnId is :"+txn.getPaymentId()+" - "+toBeRevers.getPaymentId()
						+" ; amt: "+txn.getAmt()+" - "+ toBeRevers.getAmt());
				}
				
				List<HouseholdAccTxn> households = findHouseholByPaymentId(toBeRevers.getPaymentId());
				List<HouseholdAccTxn> toCurrentHouseholds = new ArrayList<>();
				for(HouseholdAccTxn household:households) {
					if(!"1".equals(household.getIsCurrent())) {
						//不处理冲正
						toCurrentHouseholds.clear();
						break;
					}
					//删除之，并把最晚的一个设为当前
					String hql = "from HouseholdAccTxn where isCurrent = '0' and accNo=:accNo order by insertTime desc";
					@SuppressWarnings("unchecked")
					List<HouseholdAccTxn> list = entityManager.createQuery(hql).setParameter("accNo", household.getAccNo()).getResultList();
					if(list!= null && list.size()>0) {
						toCurrentHouseholds.add(list.get(0));
					}
				}
				if(!toCurrentHouseholds.isEmpty()) {
					for(HouseholdAccTxn household:toCurrentHouseholds) {
						household.setIsCurrent("1");
						household.setUpdateTime(new Timestamp(System.currentTimeMillis()));
						entityManager.merge(household);
					}
					for(HouseholdAccTxn household:households) {
						entityManager.remove(household);
					}
				}
			}
			
		}
	}
	
	private PaymentTxn filterReversBySubOrderNo(PaymentTxn orgTxn,List<PaymentTxn> txns) {
		if(orgTxn==null || StringUtils.isEmpty(orgTxn.getSubOrderNo())) {
			return null;
		}
		for(PaymentTxn txn:txns) {
			if(txn==orgTxn) {
				continue;
			}
			if (orgTxn.getSubOrderNo().equals(txn.getSubOrderNo())
					&& "1".equals(txn.getReverseFlg())
					&& txn.getPayeeNo()!=null && txn.getPayeeNo().equals(orgTxn.getPayeeNo())
					&& txn.getPayerNo()!=null && txn.getPayerNo().equals(orgTxn.getPayerNo())) {
				return txn;
			}
		}
		return null;
	}
	
	private PaymentTxn filterReversByPayOrderNo(PaymentTxn orgTxn,List<PaymentTxn> txns) {
		if(orgTxn==null || StringUtils.isEmpty(orgTxn.getPayOrderNo())) {
			return null;
		}
		for(PaymentTxn txn:txns) {
			if(txn==orgTxn) {
				continue;
			}
			if (orgTxn.getPayOrderNo().equals(txn.getPayOrderNo())
					&& "1".equals(txn.getReverseFlg())
					&& txn.getPayeeNo()!=null && txn.getPayeeNo().equals(orgTxn.getPayeeNo())
					&& txn.getPayerNo()!=null && txn.getPayerNo().equals(orgTxn.getPayerNo())) {
				return txn;
			}
		}
		return null;
	}
	
	private PaymentTxn findReversBySubOrderNo(PaymentTxn orgTxn) {
		String hql = "from PaymentTxn where subOrderNo = :subOrderNo and reverseFlg = '1' and payerNo=:payerNo and payeeNo=:payeeNo ";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(hql)
			.setParameter("subOrderNo", orgTxn.getSubOrderNo())
			.setParameter("payerNo", orgTxn.getPayerNo())
			.setParameter("payeeNo", orgTxn.getPayeeNo()).getResultList();
		if(list != null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	private PaymentTxn findReversByPayOrderNo(PaymentTxn orgTxn) {
		String hql = "from PaymentTxn where payOrderNo = :payOrderNo and reverseFlg = '1' and payerNo=:payerNo and payeeNo=:payeeNo";
		@SuppressWarnings("unchecked")
		List<PaymentTxn> list = entityManager.createQuery(hql)
			.setParameter("payOrderNo", orgTxn.getPayOrderNo())
			.setParameter("payerNo", orgTxn.getPayerNo())
			.setParameter("payeeNo", orgTxn.getPayeeNo()).getResultList();
		if(list != null && list.size()>0)
			return list.get(0);
		return null;
	}
	
	private List<HouseholdAccTxn> findHouseholByPaymentId(Long paymentId) {
		String hql = "from HouseholdAccTxn where paymentId = :paymentId";
		@SuppressWarnings("unchecked")
		List<HouseholdAccTxn> list = entityManager.createQuery(hql).setParameter("paymentId", paymentId).getResultList();
		return list;
	}
}
