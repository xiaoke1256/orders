package com.xiaoke1256.orders.search.task;

import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.search.common.zookeeper.BaseWatcher;

@Component
public class ZookeeperWatcher extends BaseWatcher {
	private volatile Boolean isMaster;
	
	private String serverId = Integer.toHexString(new Random().nextInt());
	
	public synchronized boolean toBeMast(String nodePath) throws InterruptedException {
		isMaster = null;
		while(true) {
			try {
				this.zooKeeper.create(nodePath, serverId.getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				isMaster = true;
				return isMaster;
			} catch (KeeperException e) {
				switch (e.code()) {
				case NODEEXISTS:
					if(checkMaster(nodePath))
						return isMaster;
				default:
					//其他异常（含ConnectLossException）
					if(checkMaster(nodePath))
						return isMaster;
				}
			}
		}
	}
	
	/**
	 * Check if the note has created.
	 * @param nodePath
	 * @return success or not
	 * @throws InterruptedException
	 */
	private boolean checkMaster(String nodePath) throws InterruptedException {
		while(true) {
			try {
				Stat stat = new Stat();
				byte[] data = zooKeeper.getData(nodePath, false, stat);
				isMaster = new String(data).equals(serverId);
				return true;
			} catch (KeeperException e) {
				switch (e.code()) {
				case NONODE:
					return false;
				default:
					//其他异常（含ConnectLossException）. Read it again.
				}
			}
		}
			
		
	}

}
