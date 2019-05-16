package com.xiaoke1256.thirdpay.payplatform.service;

import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${zookeeper.url}") 
	private String zookeeperUrl;
	
	@Value("${zookeeper.timeout}") 
	private int timeOut;
	
	@Bean
	public MasterWatcher exporedTaskWatcher() {
		MasterWatcher watcher = new MasterWatcher("/zookeeper/3rdpay/expired_order/master");
		watcher.setTimeOut(timeOut);
		watcher.setZookeeperUrl(zookeeperUrl);
		return watcher;
	}
}
