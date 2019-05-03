package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.core.bo.SubOrder;

@Service
@Transactional
public class SendExpiredService {
	private static  final Logger logger = LoggerFactory.getLogger(SendExpiredService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	public void sendExpired(String subOrderNo) {
		//检查订单状态
		SubOrder subOrder = entityManager.find(SubOrder.class, subOrderNo, LockModeType.PESSIMISTIC_WRITE);
		if(subOrder==null) {
			throw new BusinessException(ErrorCode.BUSSNESS_ERROR.getCode(),"The order no is not exist!");
		}
		if(!SubOrder.ORDER_STATUS_SENDING.equals(subOrder.getStatus())) {
			logger.error("Wrong order status!(order no is {}, order status is {})",subOrder.getOrderNo(),subOrder.getStatus());
			throw new BusinessException(ErrorCode.BUSSNESS_ERROR.getCode(),"Wrong order status!","订单处于错误的状态。");
		}
		
		//改变订单状态
		subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_SETTLE);
		subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(subOrder);
	}
	
	public List<SubOrder> findExpiredOrders(int resultSize){
		Date now = new Date();
		Date timeLimit = DateUtil.addDays(now, -7);//7天前的发的货
		String ql = " from SubOrder where  status = :status and updateTime<:timeLimit order by updateTime";
		@SuppressWarnings("unchecked")
		List<SubOrder> resultList = entityManager.createQuery(ql)
			.setParameter("status", SubOrder.ORDER_STATUS_SENDING)
			.setParameter("timeLimit", timeLimit).setMaxResults(resultSize).getResultList();
		
		return resultList;
	}
}
