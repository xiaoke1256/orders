package com.xiaoke1256.orders.core.task;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;
import com.xiaoke1256.orders.core.service.AccService;

/**
 * 
 * @author Administrator
 *
 */
@Component
public class HouseholdTask {
	private static final Logger logger = LoggerFactory.getLogger(HouseholdTask.class);
	
	@Resource(name="householdTaskWatcher")
	private MasterWatcher watcher;
	
	@Autowired
	private AccService accService;
	
	/**
	 * 开始记录分户账
	 */
	@Scheduled(cron="${logistics.save_household.task.corn}")
	public void startHousehold() {
		try {
			if(!watcher.toBeMast()) {
				return;
			}
			accService.doHousehold();
		}catch(InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
