package com.xiaoke1256.orders.core.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiaoke1256.orders.common.zookeeper.Client;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

@Configuration
public class WatcherConifg {
	private static final String MAKE_MONET_PATH = "/zookeeper/orders/make_money";
	private static final String SENDING_EXPIRED_PATH = "/zookeeper/orders/sending_expired";
	public static final String SETTLE_PATH = "/zookeeper/orders/settle";
	
	@Bean
	public MasterWatcher makeMoneyTaskWatcher() {
		return new MasterWatcher(MAKE_MONET_PATH);
	}
	
	@Bean
	public MasterWatcher sendingExpiredTaskWatcher() {
		return new MasterWatcher(SENDING_EXPIRED_PATH);
	}
	
	@Bean
	public MasterWatcher settlementTaskWatcher() {
		return new MasterWatcher(SETTLE_PATH);
	}
	
	@Bean
	public Client settleClient() {
		return new Client(SETTLE_PATH);
	}
}
