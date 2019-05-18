package com.xiaoke1256.orders.common.zookeeper;

import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client extends BaseWatcher {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);
	
	private String baseNodePath;
	
	/**
	 * Construct
	 * @param baseNodePath
	 */
	public Client(String baseNodePath) {
		super();
		this.baseNodePath = baseNodePath;
	}

	public String queueCommand(String command) throws KeeperException,InterruptedException {
		while(true) {
			try {
				String name = zooKeeper.create(baseNodePath+"/tasks/task-", command.getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
				return name;
			} catch (NodeExistsException e) {
				throw new RuntimeException("%s alrealy appear to be runing.",e);
			} catch (ConnectionLossException e) {
				//do nothing.
			} 
		}
	}
	
	
}
