package com.xiaoke1256.orders.core.task;

import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

@Component
public class SettlementTaskWatcher extends MasterWatcher {
	
	private final static String NODE_PATH = "/zookeeper/orders/settle/master";

	@Override
	protected String getNodePath() {
		return NODE_PATH;
	}

}
