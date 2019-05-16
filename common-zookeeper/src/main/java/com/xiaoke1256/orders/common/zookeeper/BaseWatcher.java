package com.xiaoke1256.orders.common.zookeeper;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseWatcher implements Watcher {
	private static final Logger logger = LoggerFactory.getLogger(BaseWatcher.class);
	
	private String zookeeperUrl;
	
	private int timeOut;
	
	protected ZooKeeper zooKeeper;

	public void setZookeeperUrl(String zookeeperUrl) {
		this.zookeeperUrl = zookeeperUrl;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	@PostConstruct
	public void init() throws IOException {
		zooKeeper = new ZooKeeper(zookeeperUrl,timeOut,this);
	}

	@Override
	public void process(WatchedEvent event) {
		logger.debug("Event is {}", event);
	}

}
