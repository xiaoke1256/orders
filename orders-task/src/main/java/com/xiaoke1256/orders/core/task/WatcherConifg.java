package com.xiaoke1256.orders.core.task;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

public class WatcherConifg {
	@Bean
	public MasterWatcher makeMoneyTaskWatcher() {
		return new MasterWatcher("/zookeeper/orders/make_money/master");
	}
	
	@Bean
	public MasterWatcher sendingExpiredTaskWatcher() {
		return new MasterWatcher("/zookeeper/orders/sending_expired/master");
	}
	
	@Bean
	public MasterWatcher settlementTaskWatcher() {
		return new MasterWatcher("/zookeeper/orders/settle/master");
	}
}
