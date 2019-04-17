package com.xiaoke1256.orders.thirdpayplatform.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.thirdpayplatform.bo.ThirdPayOrder;

@Service
@Transactional
public class ThirdPayService {
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	public ThirdPayOrder pay(String payerNo,String payeeNo,BigDecimal amt, String incident, String remark) {
		ThirdPayOrder order = new ThirdPayOrder();
		order.setOrderNo(genOrderNo());
		order.setPayerNo(payerNo);
		order.setPayeeNo(payeeNo);
		order.setAmt(amt);
		order.setIncident(incident);
		order.setRemark(remark);
		order.setInsertTime(new Timestamp(System.currentTimeMillis()));
		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(order);
		return order;
	}
	
	/**
	 * 生成订单号
	 * 订单号规则:年月日时分秒+6位流水号。
	 * @param subOrder
	 * @return
	 */
	private String genOrderNo() {
		return DateUtil.format(new Date(), "YYYYMMddHHmmss")
				+StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(1000000)),6);
	}
	
	/**
	 * 根据订单号获取订单。
	 * @param orderNo
	 * @return
	 */
	public ThirdPayOrder getByOrderNo(String orderNo) {
		String jql = "from ThirdPayOrder where orderNo = :orderNo";
		return (ThirdPayOrder)entityManager.createQuery(jql).setParameter("orderNo", orderNo).getSingleResult();
	}
}
