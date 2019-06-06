package com.xiaoke1256.orders.common.zookeeper;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooKeeper.States;
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
	public void init() throws IOException, InterruptedException {
		while(true) {
			zooKeeper = new ZooKeeper(zookeeperUrl,timeOut,this);
			States stat = zooKeeper.getState();
			if(stat.isConnected()) {
				return;
			} else if(States.AUTH_FAILED.equals(stat)) {
				throw new RuntimeException("Zookeeper AuthFailed!");
			}else {
				logger.error("connect fail {}.",stat);
			}
			Thread.sleep(1000);//一秒钟后重连.
		}
	}
	
	/**
	 * Expired后，重新连接时要做的工作。
	 */
	protected abstract void reboot() throws InterruptedException;

	@Override
	public void process(WatchedEvent event) {
		logger.debug("Event is {}", event);
		if(KeeperState.SyncConnected.equals(event.getState())) {
			logger.debug("connected.");
		}else if(KeeperState.Expired.equals(event.getState())) {
			logger.warn("Session Expired! class: {}",this.getClass().getName());
			synchronized(this) {
				close();
				try {
					init();
					reboot();
				} catch (IOException | InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			logger.info("Session recreated! class: {}",this.getClass().getName());
		}
	}
	
	@PreDestroy
	public synchronized void close() {
		if (zooKeeper != null) {
	        try {
	        	zooKeeper.close();
	        	zooKeeper = null;
	        } catch (InterruptedException e) {
	            //ignore exception
	        	logger.warn("Close fail!");
	        }
	    }
	}

}
