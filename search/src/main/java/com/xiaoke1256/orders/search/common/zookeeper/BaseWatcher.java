package com.xiaoke1256.orders.search.common.zookeeper;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

@PropertySource("classpath:config/zookeeper.properties")
@TestPropertySource("classpath:config/zookeeper.properties")
public abstract class BaseWatcher implements Watcher {
	@Value("${zookeeper.url}") 
	private String zookeeperUrl;
	
	@Value("${zookeeper.timeout}") 
	private int timeOut;
	
	protected ZooKeeper zooKeeper;
	
	@PostConstruct
	public void init() throws IOException {
		zooKeeper = new ZooKeeper(zookeeperUrl,timeOut,this);
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println(event);
	}

}
