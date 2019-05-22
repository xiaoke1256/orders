package com.xiaoke1256.orders.common.zookeeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
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
	
	private Executor executor = Executors.newFixedThreadPool(10);

	
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
				//节点创建成功，则开始监控任务
				getTasks();
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
	
	private Watcher newTaskWatcher = new Watcher() {

		@Override
		public void process(WatchedEvent event) {
			if(EventType.NodeChildrenChanged.equals(event.getType())) {
				assert new String(baseNodePath+"/assign/worker-"+serverId).equals(event.getPath());
				getTasks();
			}
		}
	};
	
	private void getTasks() {
		zooKeeper.getChildren(baseNodePath+"/assign/worker-"+serverId, newTaskWatcher,tasksGetChildrenCallback,null);
	}
	
	private Set<String> onGoingTasks = new HashSet<>();
	
	private ChildrenCallback tasksGetChildrenCallback = new ChildrenCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				getTasks();
				break;
			case OK:
				if(children!=null && children.size()>0) {
					updateStatus("Busy");
					executor.execute(() -> {
						synchronized (onGoingTasks) {
							for(String task:children) {
								if(!onGoingTasks.contains(task)) {
									logger.trace("New task {}",task);
									getTaskData(task);
									onGoingTasks.add(task);
								}
							}
						}
						
					});
				}else {
					//该节点没有任务
					updateStatus("Idle");
				}
				break;
			default:
				logger.error("Get children fail: ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};
	
	private void getTaskData(String task) {
		zooKeeper.getData(baseNodePath+"/assign/worker-"+serverId+"/"+task, false, excuteTaskCallback,task);
	}
	
	private DataCallback excuteTaskCallback = new DataCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			// TODO Auto-generated method stub
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
				getTaskData((String)ctx);
				break;
			case OK:
				doBusiness((String)ctx);
				finishTask();
				break;
			default:
				logger.error("Get TaskData fail: ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};

	private void finishTask() {
		
	}

	protected void doBusiness(String ctx) {
		// TODO Auto-generated method stub
		
	}
	
}
