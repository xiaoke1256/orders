package com.xiaoke1256.orders.common.zookeeper;

import java.util.List;
import java.util.Random;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Worker extends BaseWatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(Worker.class);
	
	private String serverId = Integer.toHexString(new Random().nextInt());
	
	private String baseNodePath;
	
	private String status = "Idle";

	
	/**
	 * Construct
	 * @param baseNodePath
	 */
	public Worker(String baseNodePath) {
		super();
		this.baseNodePath = baseNodePath;
	}

	public void register() {
		zooKeeper.create(baseNodePath+"/workers/worker-"+serverId,
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
			zooKeeper.setData(baseNodePath+"/workers/worker-"+serverId, status.getBytes(), -1,statusUpdateCallback,status);
		}
		
	}
	
	private Watcher worksChangeWatcher = new Watcher() {

		@Override
		public void process(WatchedEvent event) {
			if(EventType.NodeChildrenChanged.equals(event.getType())) {
				assert (baseNodePath+"/workers").equals(event.getPath());
				getWorkers();
			}
			
		}
	};
	
	private ChildrenCallback worksGetChildrenCallback = new ChildrenCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				getWorkers();
				break;
			case OK:
				logger.info("Successful get work list");
				reassignAndSet(children);
				break;
			default:
				logger.error("get children fail: ",KeeperException.create(Code.get(rc),path));
			}
			
		}

		
	};

	private ChildrenCache workersCase;
	
	protected void getWorkers() {
		zooKeeper.getChildren(baseNodePath+"/workers", worksChangeWatcher,worksGetChildrenCallback,null);
	}

	protected void reassignAndSet(List<String> children) {
		List<String> toProcess = null;
		if(workersCase == null) {
			workersCase = new ChildrenCache(children);
		} else {
			logger.info("Removing and setting");
			toProcess = workersCase.removedAndSet(children);
		}
		
		if(toProcess != null) {
			for(String worker:toProcess) {
				getAbsentWorkerTasks(worker);
			}
		}
	}

	private void getAbsentWorkerTasks(String worker) {
		// TODO Auto-generated method stub
		
	}
}
