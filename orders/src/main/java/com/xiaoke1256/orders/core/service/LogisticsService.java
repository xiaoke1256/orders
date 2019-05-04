package com.xiaoke1256.orders.core.service;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.core.bo.LoOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;

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
		//检查订单状态
		SubOrder subOrder = entityManager.find(SubOrder.class, subOrderNo,LockModeType.PESSIMISTIC_WRITE);
		if(subOrder==null) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order no is not exist!");
		}
		if(!SubOrder.ORDER_STATUS_AWAIT_SEND.equals(subOrder.getStatus())
				&&!SubOrder.ORDER_STATUS_AWAIT_ACCEPT.equals(subOrder.getStatus())) {
			logger.error("Wrong order status!(order no is {}, order status is {})",subOrder.getOrderNo(),subOrder.getStatus());
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"Wrong order status!","订单处于错误的状态。");
		}
		//同一物流公司物流单号不能重复
		LoOrder order = findByCompanyAndNo(companyCode,loOrderNo);
		if(order!=null)
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"LoOrderNo can not repeat in same company.","同一物流公司下，物流单号不能重复。");
		//保存物流单信息
		order = new LoOrder();
		order.setCompanyCode(companyCode);
		order.setLoOrderNo(loOrderNo);
		order.setSubOrderNo(subOrderNo);
		order.setCarriageAmt(subOrder.getCarriageAmt());//这里先简单得把物流费用复制过来，实际上“预收物流费用”和“实际物流费用”是不一样的
		order.setInsertTime(new Timestamp(System.currentTimeMillis()));
		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(order);
		
		//改变订单状态
		subOrder.setStatus(SubOrder.ORDER_STATUS_SENDING);
		subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(subOrder);
	}
	
	public LoOrder findByCompanyAndNo(String companyCode,String loOrderNo) {
		String ql = " from LoOrder where  companyCode = :companyCode and loOrderNo = :loOrderNo";
		Query query = entityManager.createQuery(ql)
			.setParameter("companyCode", companyCode)
			.setParameter("loOrderNo", loOrderNo);
		@SuppressWarnings("unchecked")
		List<LoOrder> list = query.getResultList();
		if(list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
	public void confirmReceived(String subOrderNo) {
		//检查订单状态
		SubOrder subOrder = entityManager.find(SubOrder.class, subOrderNo,LockModeType.PESSIMISTIC_WRITE);
		if(subOrder==null) {
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"The order no is not exist!");
		}
		if(!SubOrder.ORDER_STATUS_SENDING.equals(subOrder.getStatus())) {
			logger.error("Wrong order status!(order no is {}, order status is {})",subOrder.getOrderNo(),subOrder.getStatus());
			throw new BusinessException(RespCode.BUSSNESS_ERROR.getCode(),"Wrong order status!","订单处于错误的状态。");
		}
		
		//改变订单状态
		subOrder.setStatus(SubOrder.ORDER_STATUS_AWAIT_SETTLE);
		subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(subOrder);
	}
}
