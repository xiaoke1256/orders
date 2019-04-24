package com.xiaoke1256.orders.thirdpayplatform.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.thirdpayplatform.bo.ThirdPayOrder;
import com.xiaoke1256.orders.thirdpayplatform.dao.ThirdPayOrderDao;
import com.xiaoke1256.orders.thirdpayplatform.dto.PaymentCancelRequest;

@Service
@Transactional
public class ThirdPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(ThirdPayService.class);
	
	@Autowired
	private ThirdPayOrderDao thirdPayOrderDao ;
	
	@Value("${third_pay_platform.notice.uri}")
	private String noticeUri;//反馈接口
	
	private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * 支付
	 * @param payerNo
	 * @param payeeNo
	 * @param amt
	 * @param orderType
	 * @param remark
	 * @return
	 */
	public ThirdPayOrder pay(String payerNo,String payeeNo,BigDecimal amt,String orderType,String palteform, String remark) {
		ThirdPayOrder order = new ThirdPayOrder();
		order.setOrderNo(genOrderNo());
		order.setPayerNo(payerNo);
		order.setPayeeNo(payeeNo);
		order.setOrderType(orderType);
		order.setAmt(amt);
		order.setPalteform(palteform);
		order.setRemark(remark);
		order.setInsertTime(new Timestamp(System.currentTimeMillis()));
		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		thirdPayOrderDao.save(order);
		return order;
	}
	
	/**
	 * 订单处理成功
	 * @param orderNo
	 */
	public void success(String orderNo) {
//		ThirdPayOrder order = this.getByOrderNo(orderNo);
//		order.setOrderStatus(ThirdPayOrder.STATUS_SUCCESS);
//		order.setFinishTime(new Timestamp(System.currentTimeMillis()));
//		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Timestamp now = new Timestamp(System.currentTimeMillis());
		thirdPayOrderDao.updateStatus(orderNo, ThirdPayOrder.STATUS_SUCCESS, now, now);
		//entityManager.merge(order);
	}
	
	/**
	 * 订单处理失败
	 * @param orderNo
	 */
	public void fail(String orderNo) {
//		ThirdPayOrder order = this.getByOrderNo(orderNo);
//		order.setOrderStatus(ThirdPayOrder.STATUS_FAIL);
//		order.setFinishTime(new Timestamp(System.currentTimeMillis()));
//		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//		entityManager.merge(order);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		thirdPayOrderDao.updateStatus(orderNo, ThirdPayOrder.STATUS_FAIL, now, now);
	}
	
	/**
	 * 生成订单号
	 * 订单号规则:年月日时分秒+6位流水号。
	 * @param subOrder
	 * @return
	 */
	private String genOrderNo() {
		return DateUtil.format(new Date(), "yyyyMMddHHmmss")
				+StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(1000000)),6);
	}
	
	/**
	 * 根据订单号获取订单。
	 * @param orderNo
	 * @return
	 */
	@Transactional(readOnly=true)
	public ThirdPayOrder getByOrderNo(String orderNo) {
		return thirdPayOrderDao.findByOrderNo(orderNo);
//		String jql = "from ThirdPayOrder where orderNo = :orderNo";
//		@SuppressWarnings("unchecked")
//		List<ThirdPayOrder> list = entityManager.createQuery(jql).setParameter("orderNo", orderNo).getResultList();
//		if(list!=null&&list.size()>0)
//			return list.get(0);
//		return null;
	}
	
	/**
	 * 查出超时的订单
	 * @return 订单号列表
	 */
	@Transactional(readOnly=true)
	public List<String> queryExired(int limit) {
		Date now = new Date();
		Date expiredTime = DateUtil.addSeconds(now, -5);//5秒内没有支付完毕，就是超时的
		List<String> result = thirdPayOrderDao.findOrderNosByLimitTime(ThirdPayOrder.STATUS_ACCEPT, expiredTime);
//		String jql = "select o.orderNo from ThirdPayOrder o where orderStatus=:orderStatus and insertTime<:expiredTime order by orderId";
//		Query query = entityManager.createQuery(jql)
//			.setParameter("orderStatus", ThirdPayOrder.STATUS_ACCEPT)
//			.setParameter("expiredTime", expiredTime);
//		if(limit>0) {
//			query.setMaxResults(limit);
//		}
//		@SuppressWarnings("unchecked")
//		List<String> result = query.getResultList();
		return result;
	}
	
	/**
	 * 处理超时
	 * @param orderNo
	 */
	public void expired(String orderNo) {
		//修改订单状态
		ThirdPayOrder order = getByOrderNo(orderNo);
//		order.setOrderStatus(ThirdPayOrder.STATUS_EXPIRED);
//		order.setFinishTime(new Timestamp(System.currentTimeMillis()));
//		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//		entityManager.merge(order);
		Timestamp now = new Timestamp(System.currentTimeMillis());
		thirdPayOrderDao.updateStatus(orderNo, ThirdPayOrder.STATUS_EXPIRED, now, now);
		//接入平台方提供的接口地址。通知其超时。
		//若调用不成功则重复3次。
		PaymentCancelRequest request = new PaymentCancelRequest(order.getOrderNo(),order.getRemark(),PaymentCancelRequest.CANCEL_TYPE_EXPIRED);
		for(int i = 0;i<3;i++) {
			RespMsg resp = restTemplate.postForObject(noticeUri, request, RespMsg.class);//TODO 此处有个风险，万一调用时间过长，有长时间锁表风险。
			if(RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
				return;
			}
			logger.error("Remote notice the commercial tenant error . code : %s - msg: %s ",resp.getCode(),resp.getMsg());
		}

		//再失败则标记为需人工处理，打印日志。
//		order.setOrderStatus(ThirdPayOrder.STATUS_NEED_MANNUAL);
//		order.setFinishTime(null);
//		order.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//		entityManager.merge(order);
		thirdPayOrderDao.updateStatus(orderNo, ThirdPayOrder.STATUS_NEED_MANNUAL, null, now);
		logger.error("Remote notice fail for 10 times.");
	}
}
