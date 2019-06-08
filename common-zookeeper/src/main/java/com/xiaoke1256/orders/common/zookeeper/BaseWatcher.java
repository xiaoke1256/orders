package com.xiaoke1256.orders.common.zookeeper;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
	
	protected volatile ZooKeeper zooKeeper;
	
	@PostConstruct
	public void initZk() throws InterruptedException {
		new Thread(()->{
			try {
				connect();
			} catch (IOException | InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
		}).start();
	}
	
	private void connect() throws IOException, InterruptedException {
		while(true) {
			int checkTims = 50;
			zooKeeper = new ZooKeeper(zookeeperUrl,timeOut,this);
			while(checkTims>=0) {
				Thread.sleep(30);//30毫秒后检查连接情况.
				States stat = zooKeeper.getState();
				if(stat.isConnected()) {
					return;
				} else if(States.AUTH_FAILED.equals(stat)) {
					throw new RuntimeException("Zookeeper AuthFailed!");
				}else {
					logger.error("connect fail {}.",stat.toString());
				}
				checkTims--;
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
					connect();
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
