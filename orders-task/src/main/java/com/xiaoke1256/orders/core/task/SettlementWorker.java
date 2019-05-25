package com.xiaoke1256.orders.core.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.zookeeper.Worker;
import com.xiaoke1256.orders.core.service.SettleService;

public class SettlementWorker extends Worker {
	
	@Autowired
	private SettleService settleService;

	public SettlementWorker() {
		super(WatcherConifg.SETTLE_PATH);
	}

	@Override
	protected void doBusiness(String data) {
		if(data.isEmpty())
			throw new AppException(RespCode.EMPTY_PARAMTER_ERROR);
		String[] paras = data.split(",");
		if(paras.length!=3)
			throw new AppException(RespCode.WRONG_PARAMTER_ERROR);
		
		String storeNo = paras[0];
		String year = paras[1];
		String month = paras[2];
		
		settleService.genSettleStatemt(storeNo, year, month);
	}

}
