package com.xiaoke1256.orders.core.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;
import static com.xiaoke1256.orders.core.bo.SettleStatemt.*;
import com.xiaoke1256.orders.core.client.MakeMoneyClient;
import com.xiaoke1256.orders.core.dto.SettleStatemt;
import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;
import com.xiaoke1256.orders.core.dto.SettleStatemtQueryResultResp;
/**
 * 批量打款定时任务
 * @author Administrator
 *
 */
@Component
public class MakeMoneyTask {
	private static final Logger logger = LoggerFactory.getLogger(MakeMoneyTask.class);
	
	@Autowired
	private MakeMoneyTaskWatcher watcher;
	
	@Autowired
	private MakeMoneyClient makeMoneyClient;
	
	//@Scheduled(cron="${logistics.make_money.task.corn}")
	public void startMakeMoneys() {
		try {
			if(!watcher.toBeMast()) {
				return;
			}
			Date todate = new Date();
			Date lastMonth = DateUtil.addMonth(todate, -1);
			
			//查询所有上个月度待打款的清算单
			SettleStatemtCondition condition = new SettleStatemtCondition();
			condition.setYear(String.valueOf(DateUtil.getYear(lastMonth)));
			condition.setMonth(DateUtil.format(lastMonth, "MM"));
			condition.setStatus(STATUS_AWAIT_MAKE_MONEY);//
			condition.setPageSize(Integer.MAX_VALUE);
			SettleStatemtQueryResultResp settles = makeMoneyClient.searchSettle(condition);
			
			//打款
			for(SettleStatemt settle:settles.getResultList()) {
				makeMoneyClient.makeMoney(settle.getSettleNo());
			}
			
		}catch(InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
