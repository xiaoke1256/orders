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

	public void submitTask(String task,TaskObject taskCtx) {
		taskCtx.setTask(task);
		zooKeeper.create(baseNodePath+"/tasks/task-", task.getBytes(),Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT_SEQUENTIAL,createTaskCallback,taskCtx);
		
	}
	
	private StringCallback createTaskCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			// TODO Auto-generated method stub
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				submitTask(((TaskObject)ctx).getTask(),(TaskObject)ctx);
				break;
			case OK:
				logger.info("My create task name: {}",name);
				watchStatus(baseNodePath+"/status/"+name.replace(baseNodePath+"/tasks", ""),ctx);
				break;
			default:
				logger.error("Submit task fail: ",KeeperException.create(Code.get(rc),path));
			}
		}

		private void watchStatus(String string, Object ctx) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	
	
}
