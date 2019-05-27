package com.xiaoke1256.orders.core.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.Worker;
import com.xiaoke1256.orders.core.client.MakeMoneyClient;

@Component
public class MakeMoneyWorker extends Worker {
	
	@Autowired
	private MakeMoneyClient makeMoneyClient;

	public MakeMoneyWorker() {
		super(WatcherConifg.MAKE_MONET_PATH);
	}

	@Override
	protected void doBusiness(String businessData) {
		String settleNo = businessData;
		makeMoneyClient.makeMoney(settleNo );
	}

}
