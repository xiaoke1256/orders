package com.xiaoke1256.orders.core.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.core.bo.SettleStatemt;
import com.xiaoke1256.orders.core.bo.SubOrder;

/**
 * 清结算有关的业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class SettleService {
	private static final Logger logger = LoggerFactory.getLogger(SettleService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager;
	/**
	 * 按商户号查询待清结算的订单
	 */
	public List<SubOrder> queryAwaitSettleOrders(String storeNo,String year,String month){
		Date endOfMonth = DateUtil.getLastTimeOfMonth(DateUtil.parseDate(year+"-"+month, "yyyy-MM"));
		String ql = "from SubOrder where storeNo = :storeNo and status = :status and updateTime<=:endOfMonth";
		@SuppressWarnings("unchecked")
		List<SubOrder> result = entityManager.createQuery(ql)
			.setParameter("storeNo", storeNo)
			.setParameter("status", SubOrder.ORDER_STATUS_AWAIT_SETTLE)
			.setParameter("endOfMonth", endOfMonth).getResultList();
		return result;
	} 
	
	
	/**
	 * 生成结算单
	 */
	public void genSettleStatemt(String storeNo,String year,String month) {
		//TODO 先查本月有没有已存在的结算单，如有则先废除掉。
		
		//查待清算的订单。
		SettleStatemt settleStatemt = new SettleStatemt();
		List<SubOrder> orders = queryAwaitSettleOrders(storeNo,year,month);
		//生成结算项
		for(SubOrder order:orders) {
			//设置订单总额。
			//计算佣金
		}
		
		//计算订单总额合计
		//计算佣金合计
		//设置月租金
		//计算待结款
		
		//保存
		entityManager.persist(settleStatemt);
		//修改订单状态。
	}
}
