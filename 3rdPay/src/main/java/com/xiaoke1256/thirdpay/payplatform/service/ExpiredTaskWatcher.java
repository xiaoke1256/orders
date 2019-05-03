package com.xiaoke1256.thirdpay.payplatform.service;

import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

/**
 * zookeeper 协调器，用户确定谁是群主。
 * @author Administrator
 *
 */
@Component
public class ExpiredTaskWatcher extends MasterWatcher {
	
	private final String NODE_PATH = "/zookeeper/3rdpay/expired_order/master";

	@Override
	protected String getNodePath() {
		return NODE_PATH;
	}

}
