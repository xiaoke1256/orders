package com.xiaoke1256.orders.core.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;

/**
 * 清算定时任务
 * @author Administrator
 *
 */
@Component
public class SettlementTask {
	
	@Autowired
	private StoreQueryService storeQueryService;
	
	/**
	 * 清算
	 */
	public void settlement() {
		//远程调用查询商户信息
		List<Store> stores = storeQueryService.queryAvailableStore();
		//根据当前日期，决定清算的报告期。规则：小于5号，则清算上一月度；大于5号则清算本月度
		Date reportDate = new Date();
		int date = DateUtil.getDate(reportDate);
		if(date<=5)
			reportDate = DateUtil.addMonth(reportDate, -1);
		String year = String.valueOf(DateUtil.getYear(reportDate));
		String month = String.valueOf(DateUtil.getMonth(reportDate));
	}
}
