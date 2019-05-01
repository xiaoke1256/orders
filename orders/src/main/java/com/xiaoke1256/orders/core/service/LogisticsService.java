package com.xiaoke1256.orders.core.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogisticsService {
	
	private static  final Logger logger = LoggerFactory.getLogger(LogisticsService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	/**
	 * 提交物流单
	 * @param subOrderNo 订单号
	 * @param companyCode 物流公司代码
	 * @param loOrderNo 物流单号
	 */
	public void submitLoOrder(String subOrderNo,String companyCode,String loOrderNo) {
		
	}
}
