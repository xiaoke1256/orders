package com.xiaoke1256.orders.common.zookeeper;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public abstract class BaseWatcher implements Watcher {
	private static final Logger logger = LoggerFactory.getLogger(BaseWatcher.class);
	
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
		logger.debug("Event is {}", event);
	}

}
