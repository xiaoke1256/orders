package com.xiaoke1256.orders.core.service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.xiaoke1256.orders.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.OStorage;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class OStorageService {
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Transactional(readOnly=true)
	public OStorage getByProductCode(String productCode) {
		String jql = "from OStorage where productCode=:productCode and optionCode is null";
		List<OStorage> results = entityManager.createQuery(jql).setParameter("productCode", productCode).getResultList();
		if(results.isEmpty()){
			return null;
		}else{
			return results.get(0);
		}

	}
	
	@Transactional(readOnly=true)
	public OStorage getByProductCodeAndOptionCode(String productCode,String optionCode) {
		String jql = "from OStorage where productCode=:productCode ";
		if (StringUtils.isNotEmpty(optionCode)){
			jql += "and optionCode=:optionCode";
		}else{
			jql += "and optionCode is null";
		}
		Query query = entityManager.createQuery(jql)
				.setParameter("productCode", productCode);
		if(StringUtils.isNotEmpty(optionCode)){
			query.setParameter("optionCode", optionCode);
		}

		List<OStorage> results = entityManager.createQuery(jql)
				.setParameter("productCode", productCode)
				.getResultList();
		if(results.isEmpty()){
			return null;
		}else{
			return results.get(0);
		}
	}

	@Transactional(readOnly=true)
	public long getStorageNumByProductCode(String productCode){
		String jql = "select sum(o.stockNum) from OStorage o where o.productCode=:productCode";
		Number results = (Number)entityManager.createQuery(jql)
				.setParameter("productCode", productCode)
				.getSingleResult();
		if(results!=null) {
			return results.longValue();
		}else{
			return 0;
		}
	}

	/**
	 * 添加库存
	 * @param productCode
	 * @param incNum
	 */
	public void incStorage(String productCode,String optionCode,int incNum){
		OStorage storage = this.getByProductCodeAndOptionCode(productCode, optionCode);
		if(storage!=null) {
			entityManager.lock(storage, LockModeType.PESSIMISTIC_WRITE);
			if (storage.getStockNum() + incNum < 0) {
				throw new BusinessException("库存不可降至0以下");
			}
			storage.setStockNum(storage.getStockNum() + incNum);
			entityManager.merge(storage);
		}else {
			storage = new OStorage();
			storage.setProductCode(productCode);
			storage.setOptionCode(optionCode);
			storage.setStockNum((long)incNum);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			storage.setInsertTime(now);
			storage.setUpdateTime(now);
			entityManager.persist(storage);
		}
	}
}
