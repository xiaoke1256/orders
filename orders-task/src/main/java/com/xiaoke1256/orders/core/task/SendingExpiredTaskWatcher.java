package com.xiaoke1256.orders.core.task;

import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

/**
 * zookeeper 协调器，用户确定谁是群主。
 * @author Administrator
 *
 */
@Component
public class SendingExpiredTaskWatcher extends MasterWatcher {
	
	private final static String NODE_PATH = "/zookeeper/orders/sending_expired/master";

	@Override
	protected String getNodePath() {
		return NODE_PATH;
	}

}
