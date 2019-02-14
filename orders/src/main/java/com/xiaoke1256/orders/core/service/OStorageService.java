package com.xiaoke1256.orders.core.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.OStorage;

@Service
@Transactional
public class OStorageService {
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Transactional(readOnly=true)
	public OStorage getByProductCode(String productCode) {
		String jql = "from OStorage where productCode=:productCode and optionCode is null";
		OStorage result = (OStorage)entityManager.createQuery(jql).setParameter("productCode", productCode).getSingleResult();
		return result;
	}
	
	@Transactional(readOnly=true)
	public OStorage getByProductCodeAndOptionCode(String productCode,String optionCode) {
		String jql = "from OStorage where productCode=:productCode and optionCode:=optionCode";
		OStorage result = (OStorage)entityManager.createQuery(jql)
				.setParameter("productCode", productCode)
				.setParameter("optionCode", optionCode).getSingleResult();
		return result;
	}
}
