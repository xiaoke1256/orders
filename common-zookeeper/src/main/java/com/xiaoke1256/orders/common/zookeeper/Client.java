package com.xiaoke1256.orders.common.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Ids;

public class Client extends BaseWatcher {
	private String baseNodePath;
	
	/**
	 * Construct
	 * @param baseNodePath
	 */
	public Client(String baseNodePath) {
		super();
		this.baseNodePath = baseNodePath;
	}

	public String queueCommand(String command) throws Exception {
		while(true) {
			try {
				String name = this.zooKeeper.create(baseNodePath+"/tasks/task-", command.getBytes(),
						Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
				return name;
			} catch (NodeExistsException e) {
				throw new RuntimeException("%s alrealy appear to be runing.",e);
			} catch (ConnectionLossException e) {
				//do nothing.
			} catch (KeeperException | InterruptedException e) {
				throw e;
			}
		}
	}
}
