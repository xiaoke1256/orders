package com.xiaoke1256.thirdpay.payplatform.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.thirdpay.payplatform.bo.HouseholdAcc;
import com.xiaoke1256.thirdpay.payplatform.dao.HouseholdAccDao;
import com.xiaoke1256.thirdpay.payplatform.dao.ThirdPayOrderDao;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;

import com.xiaoke1256.thirdpay.payplatform.dto.PaymentCancelRequest;

@Service
@Transactional
public class ThirdPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(ThirdPayService.class);
	
	@Autowired
	private ThirdPayOrderDao thirdPayOrderDao ;

	@Autowired
	private HouseholdAccDao householdAccDao;

	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	
	@Value("${third_pay_platform.notice.uri}")
	private String noticeUri;//反馈接口
	
	private RestTemplate restTemplate = new RestTemplate();
	
	/**
	 * 支付
	 * @param thirdPayerNo
	 * @param thirdPayeeNo
	 * @param amt
	 * @param orderType
	 * @param remark
	 * @return
	 */
	public ThirdPayOrder pay(String thirdPayerNo,
							 String thirdPayeeNo,
							 String merchantPayerNo,
							 String merchantPayeeNo,
							 String merchantOrderNo,
							 String bussinessNo,
							 BigDecimal amt,
							 String orderType,
							 String merchantNo,
							 String incident,
							 String remark) {

		//检查支付方的余额
		HouseholdAcc payerAcc = this.findAccountByAccNo(thirdPayerNo);
		if(payerAcc.getBalance().compareTo(amt) < 0) {
			throw new BusinessException("余额不足。", RespCode.BUSSNESS_ERROR.getCode(), "余额不足。");
		}
		ThirdPayOrder order = new ThirdPayOrder();
		order.setOrderNo(genOrderNo());
		order.setThirdPayerNo(thirdPayerNo);
		order.setThirdPayeeNo(thirdPayeeNo);
		order.setMerchantPayerNo(merchantPayerNo);
		order.setMerchantPayeeNo(merchantPayeeNo);
		order.setMerchantOrderNo(merchantOrderNo);
		order.setBussinessNo(bussinessNo);
		order.setAmt(amt);
		order.setOrderType(orderType);
		order.setMerchantNo(merchantNo);
		order.setIncident(incident);
		order.setRemark(remark);
		order.setInsertTime(LocalDateTime.now());
		order.setUpdateTime(LocalDateTime.now());
		thirdPayOrderDao.save(order);
		logger.info("订单生成成功:"+order.getOrderNo());
		//发消息后续处理
		rocketMQTemplate.syncSend("3rdPay_post_payment",order.getOrderNo());
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
	 * @param
	 * @return
	 */
	private String genOrderNo() {
		return DateUtil.format(new Date(), "yyyyMMddHHmmss")
				+StringUtils.leftPad(String.valueOf(RandomUtils.nextInt(1000000)),6,'0');
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

	public HouseholdAcc findAccountByName(String accountName) {
		LambdaQueryWrapper<HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(HouseholdAcc::getAccName,accountName);
		HouseholdAcc account = householdAccDao.getOne(wrapper);
		if(account!=null){
			return account;
		}
		List<HouseholdAcc> allAccounts = householdAccDao.list();
		if(allAccounts!=null && allAccounts.size()>0 ) {
			return allAccounts.get((int)Math.floor (Math.random() * allAccounts.size()));
		}
		return null;
	}

	public HouseholdAcc findAccountByAccNo(String accNo) {
		LambdaQueryWrapper<HouseholdAcc> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(HouseholdAcc::getAccNo,accNo);
		HouseholdAcc account = householdAccDao.getOne(wrapper);
		return account;
	}
}
