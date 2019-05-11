package com.xiaoke1256.orders.core.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.core.bo.SettleStatemt;
import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;

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
	
	@Transactional(readOnly = true)
	public QueryResult<SettleStatemt> searchSettleStatemts(SettleStatemtCondition condition){
		StringBuilder qlSb = new StringBuilder("from SettleStatemt s where 1 = 1 ");
		Map<String,Object> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(condition.getSettleNo())) {
			qlSb.append(" and settleNo like :settleNo ");
			paramMap.put("settleNo", condition.getSettleNo()+"%");
		}
		if(StringUtils.isNotEmpty(condition.getStoreNo())) {
			qlSb.append(" and storeNo = :storeNo ");
			paramMap.put("storeNo", condition.getStoreNo());
		}
		if(StringUtils.isNotEmpty(condition.getYear())) {
			qlSb.append(" and year = :year ");
			paramMap.put("year", condition.getYear());
		}
		if(StringUtils.isNotEmpty(condition.getMonth())) {
			qlSb.append(" and month = :month ");
			paramMap.put("month", condition.getMonth());
		}
		if(StringUtils.isNotEmpty(condition.getStatus())) {
			qlSb.append(" and status = :status ");
			paramMap.put("status", condition.getStatus());
		}
		
		String countQl = "select count(s) "+qlSb.toString();
		qlSb.append(" order by updateTime desc ");
		Query countQuery = entityManager.createQuery(countQl);
		for(String key:paramMap.keySet()) {
			countQuery.setParameter(key, paramMap.get(key));
		}
		int totalCount = ((Number)countQuery.getSingleResult()).intValue();

		Query query = entityManager.createQuery(qlSb.toString());
		for(String key:paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		
		QueryResult<SettleStatemt> page = new QueryResult<SettleStatemt>(condition.getPageNo(),condition.getPageSize(),totalCount);
		
		query.setFirstResult((condition.getPageNo()-1)*condition.getPageSize());
		query.setMaxResults(condition.getPageSize());
		@SuppressWarnings("unchecked")
		List<SettleStatemt> results = query.getResultList();
		page.setResultList(results);
		
		return page;
		
	}
	
	@Transactional(readOnly = true)
	public List<SettleStatemt> querySettleStatemts(SettleStatemtCondition condition){
		StringBuilder qlSb = new StringBuilder("from SettleStatemt s where 1 = 1 ");
		Map<String,Object> paramMap = new HashMap<>();
		if(StringUtils.isNotEmpty(condition.getSettleNo())) {
			qlSb.append(" and settleNo like :settleNo ");
			paramMap.put("settleNo", condition.getSettleNo()+"%");
		}
		if(StringUtils.isNotEmpty(condition.getStoreNo())) {
			qlSb.append(" and storeNo = :storeNo ");
			paramMap.put("storeNo", condition.getStoreNo());
		}
		if(StringUtils.isNotEmpty(condition.getYear())) {
			qlSb.append(" and year = :year ");
			paramMap.put("year", condition.getYear());
		}
		if(StringUtils.isNotEmpty(condition.getMonth())) {
			qlSb.append(" and month = :month ");
			paramMap.put("month", condition.getMonth());
		}
		if(StringUtils.isNotEmpty(condition.getStatus())) {
			qlSb.append(" and status = :status ");
			paramMap.put("status", condition.getStatus());
		}
		
		qlSb.append(" order by updateTime desc ");

		Query query = entityManager.createQuery(qlSb.toString());
		for(String key:paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		@SuppressWarnings("unchecked")
		List<SettleStatemt> results = query.getResultList();
		return results;
		
	}

}
