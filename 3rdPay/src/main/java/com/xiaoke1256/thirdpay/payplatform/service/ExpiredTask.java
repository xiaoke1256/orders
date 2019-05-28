package com.xiaoke1256.thirdpay.payplatform.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xiaoke1256.orders.common.zookeeper.Client;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

/**
 * 支付超时所要执行的定时任务
 * @author Administrator
 *
 */
@Service
public class ExpiredTask {
	private static final Logger logger = LoggerFactory.getLogger(ExpiredTask.class);
	
	@Resource(name="exporedTaskWatcher")
	private MasterWatcher masterWatcher;
	

	@Resource(name="expiredZkClient")
	private Client zkClient;
	
	@Autowired
	private ThirdPayService thirdPayService;
	
	@Value("${third_pay_platform.expired.list.max_result}")
	private int limit;
	
	@Scheduled(cron="${third_pay_platform.expired.task.corn}")
	public void makeOrdersExpired() {
		try {
			if(!masterWatcher.toBeMast()) {
				logger.debug("I'm not master. Do nothong.");
				return;
			}
			if(!zkClient.isAllFinished()) {
				logger.debug("some tasks is going working!");
				return;
			}
			logger.debug("I'm master .Doing the task.");
			//查出超时的订单。
			List<String> orderNos = thirdPayService.queryExired(limit);
			for(String orderNo:orderNos) {
				zkClient.submitTask(orderNo);
			}
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
}
