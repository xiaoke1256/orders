package com.xiaoke1256.thirdpay.payplatform.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiaoke1256.orders.common.zookeeper.MasterWatcher;

/**
 * 用于组装zookeeper的Watcher组件
 * @author Administrator
 *
 */
@Configuration
public class WatcherConfig {
	@Bean
	public MasterWatcher exporedTaskWatcher() {
		return new MasterWatcher("/zookeeper/3rdpay/expired_order");
	}
}
