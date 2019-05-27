package com.xiaoke1256.orders.core.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.common.zookeeper.Client;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;
import com.xiaoke1256.orders.core.client.MakeMoneyClient;
import com.xiaoke1256.orders.core.dto.SettleStatemt;
/**
 * 批量打款定时任务
 * @author Administrator
 *
 */
@Component
public class MakeMoneyTask {
	private static final Logger logger = LoggerFactory.getLogger(MakeMoneyTask.class);
	
	@Resource(name="makeMoneyTaskWatcher")
	private MasterWatcher watcher;
	
	@Autowired
	private MakeMoneyClient makeMoneyClient;
	
	@Resource(name="makeMoneyZkClient")
	private Client zkClient;
	
	@Scheduled(cron="${logistics.make_money.task.corn}")
	public void startMakeMoneys() {
		try {
			if(!watcher.toBeMast()) {
				return;
			}
			Date todate = new Date();
			Date lastMonth = DateUtil.addMonth(todate, -1);
			String year = String.valueOf(DateUtil.getYear(lastMonth));
			String month = DateUtil.format(lastMonth, "MM");
			
			//查询所有上个月度待打款的清算单
			List<SettleStatemt> settles = makeMoneyClient.queryAwaitMakeMoney(year, month);
			
			//打款
			for(SettleStatemt settle:settles) {
				zkClient.submitTask(settle.getSettleNo());
			}
			
		}catch(InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
