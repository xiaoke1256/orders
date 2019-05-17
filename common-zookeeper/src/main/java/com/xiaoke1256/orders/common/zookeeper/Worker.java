package com.xiaoke1256.orders.common.zookeeper;

import java.util.Random;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker extends BaseWatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);
	
	private String serverId = Integer.toHexString(new Random().nextInt());
	
	private String parentNodePath;
	
	private String status;
	
	public void register() {
		zooKeeper.create(parentNodePath+"/workers/worker-"+serverId,
				"Idle".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);//"Idle" 表示空闲
	}
	
	private StringCallback createWorkerCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				register();
				break;
			case OK:
				logger.info("Regitered successfully: %s",serverId);
				break;
			case NODEEXISTS:
				logger.warn("Already registered: %s",serverId );
				break;
			default:
				logger.error("Something went wrong: ",KeeperException.create(Code.get(rc),path));
			}
			
		}
		
	};
	
	private StatCallback statusUpdateCallback = new StatCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				updateStatus((String)ctx);
				return;
			default:
				break;
			}
		}
	};

	protected void updateStatus(String status) {
		if(this.status==status) {
			zooKeeper.setData(parentNodePath+"/workers/worker-"+serverId, status.getBytes(), -1,statusUpdateCallback,status);
		}
		
	}
}
