package com.xiaoke1256.orders.common.zookeeper;

import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
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
	
	private Watcher taskChangeWatcher = new Watcher() {
		@Override
		public void process(WatchedEvent event) {
			if(EventType.NodeChildrenChanged.equals(event.getType())) {
				assert (baseNodePath+"/tasks").equals(event.getPath());
				getTasks();
			}
		}
	};
	
	private ChildrenCallback childrenCallback = new ChildrenCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children) {
			// TODO Auto-generated method stub
			
		}
		
	};

	protected void getTasks() {
		// TODO Auto-generated method stub
		zooKeeper.getChildren(baseNodePath+"/tasks", taskChangeWatcher,childrenCallback,null);
	}
}
