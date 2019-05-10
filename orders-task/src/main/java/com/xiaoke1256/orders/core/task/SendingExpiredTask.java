package com.xiaoke1256.orders.core.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.service.SendExpiredService;

/**
 * 发货到期的定时任务
 * @author Administrator
 *
 */
@Component
public class SendingExpiredTask {
	
	private static final Logger logger = LoggerFactory.getLogger(SendingExpiredTask.class);
	
	@Value("${logistics.send_expired.list.max_result}")
	private int resultSize;
	
	@Autowired
	private SendExpiredService sendExpiredService;
	
	@Autowired
	private SendingExpiredTaskWatcher sendingExpiredTaskWatcher;
	
	@Scheduled(cron="${logistics.send_expired.task.corn}")
	public void sendExpired() {
		try {
			if(!sendingExpiredTaskWatcher.toBeMast()) {
				return;
			}
			logger.info("Start send expird task.");
			List<SubOrder> orders = sendExpiredService.findExpiredOrders(resultSize);
			for(SubOrder order:orders) {
				sendExpiredService.sendExpired(order.getOrderNo());
			}
			logger.info("End of send expird task.");
		}catch(InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
