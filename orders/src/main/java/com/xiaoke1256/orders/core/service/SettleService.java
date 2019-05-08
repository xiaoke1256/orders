package com.xiaoke1256.orders.core.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.SettleStatemt;

/**
 * 清结算有关的业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class SettleService {
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager;
	
	@Transactional(readOnly = true)
	public SettleStatemt getSettleStatemtByNo(String settleNo) {
		String ql = "from SettleStatemt where settleNo = :settleNo";
		@SuppressWarnings("unchecked")
		List<SettleStatemt> list = entityManager.createQuery(ql).setParameter("settleNo", settleNo).getResultList();
		if(list!=null && list.size()>0)
			return list.get(0);
		return null;
	} 

}
