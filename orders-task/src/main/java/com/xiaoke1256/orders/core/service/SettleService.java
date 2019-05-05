package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.core.bo.SettleItemOrder;
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
		String ql = "from SubOrder where storeNo = :storeNo and status = :status and receiveTime<=:endOfMonth";
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
		//先查本月有没有已存在的结算单，如有则先废除掉。
		String ql = "from SettleStatemt where storeNo=:storeNo and year=:year and month=:month and status in ('"+SettleStatemt.STATUS_AWAIT_MAKE_MONEY+"','"+SettleStatemt.STATUS_HAS_MADE_MONEY+"')";
		@SuppressWarnings("unchecked")
		List<SettleStatemt> orgSettles = entityManager.createQuery(ql)
			.setParameter("storeNo", storeNo)
			.setParameter("year", year)
			.setParameter("month", month).getResultList();
		for(SettleStatemt orgSettle:orgSettles) {
			disableSettleStatemt(orgSettle);
		}
		
		
		//查待清算的订单。
		SettleStatemt settleStatemt = new SettleStatemt();
		List<SubOrder> orders = queryAwaitSettleOrders(storeNo,year,month);
		Set<SettleItemOrder> settleItemOrders = new HashSet<>();
		//生成结算项
		for(SubOrder order:orders) {
			SettleItemOrder item = new SettleItemOrder();
			item.setOrderNo(order.getOrderNo());
			//设置订单总额。
			item.setTotalAmt(order.getTotalAmt());
			//计算佣金(总额-运费)*0.01
			BigDecimal commission = order.getTotalAmt().subtract(order.getCarriageAmt()).multiply(BigDecimal.valueOf(0.01)).setScale(-1, RoundingMode.HALF_UP);
			if(commission.intValue()<1000) {
				commission = new BigDecimal(1000);
			}
			item.setCommission(commission);
			item.setOtherCharge(BigDecimal.ZERO);
			item.setSettleStatemt(settleStatemt);
			item.setInsertTime(new Timestamp(System.currentTimeMillis()));
			item.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			settleItemOrders.add(item);
		}
		settleStatemt.setSettleItemOrders(settleItemOrders);
		
		BigDecimal totalAmt = BigDecimal.ZERO;
		BigDecimal commission = BigDecimal.ZERO;
		for(SettleItemOrder settleItemOrder:settleItemOrders) {
			//计算订单总额合计
			totalAmt.add(settleItemOrder.getTotalAmt());
			//计算佣金合计
			commission.add(settleItemOrder.getCommission());
		}
		settleStatemt.setTotalAmt(totalAmt);
		settleStatemt.setCommission(commission);
		//设置月租金
		settleStatemt.setMonthlyCharge(BigDecimal.valueOf(500000));//月租暂时统一设置为500元
		//计算待结款
		settleStatemt.setPendingPayment(settleStatemt.getTotalAmt().subtract(settleStatemt.getCommission()).subtract(settleStatemt.getMonthlyCharge()));
		settleStatemt.setAlreadyPaid(BigDecimal.ZERO);
		
		//保存
		entityManager.persist(settleStatemt);
		//修改订单状态。
		for(SubOrder order:orders) {
			order.setStatus(SubOrder.ORDER_STATUS_AWAIT_MAKE_MONEY);
			order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(order);
		}
	}
	
	/**
	 * 废除月结单
	 * @param settleStatemt
	 */
	public void disableSettleStatemt(SettleStatemt settleStatemt) {
		if(!SettleStatemt.STATUS_AWAIT_MAKE_MONEY.equals(settleStatemt.getStatus())) {
			throw new AppException(RespCode.STATUS_ERROR);
		}
		
		settleStatemt.setStatus(SettleStatemt.STATUS_DISUSED);
		settleStatemt.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.merge(settleStatemt);
		
		for(SettleItemOrder settleItemOrder:settleStatemt.getSettleItemOrders()) {
			SubOrder order = entityManager.find(SubOrder.class, settleItemOrder.getOrderNo());
			//从待打款改成待清算
			order.setStatus(SubOrder.ORDER_STATUS_AWAIT_SETTLE);
			order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			entityManager.merge(order);
		}
		
	}
}
