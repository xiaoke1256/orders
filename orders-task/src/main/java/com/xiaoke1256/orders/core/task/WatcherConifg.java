package com.xiaoke1256.orders.core.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiaoke1256.orders.common.zookeeper.Client;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;
import com.xiaoke1256.orders.common.zookeeper.MasterWatcherWithWorkers;

@Configuration
public class WatcherConifg {
	public static final String MAKE_MONET_PATH = "/zookeeper/orders/make_money";
	private static final String SENDING_EXPIRED_PATH = "/zookeeper/orders/sending_expired";
	public static final String SETTLE_PATH = "/zookeeper/orders/settle";
	
	@Bean
	public MasterWatcher makeMoneyTaskWatcher() {
		return new MasterWatcherWithWorkers(MAKE_MONET_PATH);
	}
	
	@Bean
	public MasterWatcher sendingExpiredTaskWatcher() {
		return new MasterWatcher(SENDING_EXPIRED_PATH);
	}
	
	@Bean
	public MasterWatcher settlementTaskWatcher() {
		return new MasterWatcherWithWorkers(SETTLE_PATH);
	}
	
	@Bean
	public Client settleZkClient() {
		return new Client(SETTLE_PATH);
	}
	
	@Bean
	public Client makeMoneyZkClient() {
		return new Client(MAKE_MONET_PATH);
	}
}
