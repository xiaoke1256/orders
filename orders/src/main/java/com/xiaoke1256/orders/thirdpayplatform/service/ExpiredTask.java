package com.xiaoke1256.orders.thirdpayplatform.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 支付超时所要执行的定时任务
 * @author Administrator
 *
 */
@Service
public class ExpiredTask {
	@Autowired
	private ThirdPayService thirdPayService;
	
	@Value("${third_pay_platform.expired.limit}")
	private int limit;
	
	//@Scheduled(cron="${third_pay_platform.expired.task.corn}")
	public void makeOrdersExpired() {
		//查出超时的订单。
		List<String> orderNos = thirdPayService.queryExired(limit);
		for(String orderNo:orderNos) {
			//超时处理
			thirdPayService.expired(orderNo);
		}
		
	}
	
}
