package com.xiaoke1256.orders.core.task;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.common.zookeeper.Client;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;
import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;

/**
 * 清算定时任务
 * @author Administrator
 *
 */
@Component
public class SettlementTask {
	private static final Logger logger = LoggerFactory.getLogger(SendingExpiredTask.class);
	
	@Autowired
	private StoreQueryService storeQueryService;
	
	@Resource(name="settlementTaskWatcher")
	private MasterWatcher settlementTaskWatcher;
	
	@Resource(name="settleClient")
	private Client settleClient;
	
	/**
	 * 定时触发
	 */
	@Scheduled(cron="${logistics.settle.task.corn}")
	public void startSettlement() {
		try {
			if(!settlementTaskWatcher.toBeMast()) {
				return;
			}
			settlement();
		}catch(InterruptedException e) {
			logger.error(e.getMessage(), e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 清算
	 */
	private void settlement() {
		//远程调用查询商户信息
		List<Store> stores = storeQueryService.queryAvailableStore();
		//根据当前日期，决定清算的报告期。规则：小于5号，则清算上一月度；大于5号则清算本月度
		Date reportDate = new Date();
		int date = DateUtil.getDate(reportDate);
		if(date<=5)
			reportDate = DateUtil.addMonth(reportDate, -1);
		String year = String.valueOf(DateUtil.getYear(reportDate));
		String month = StringUtils.leftPad(String.valueOf(DateUtil.getMonth(reportDate)), 2, '0');
		for(Store store:stores) {
			settleClient.submitTask(store.getStoreNo()+"," +year+","+ month);
			//settleService.genSettleStatemt(store.getStoreNo(), year, month);
		}
	}
}
