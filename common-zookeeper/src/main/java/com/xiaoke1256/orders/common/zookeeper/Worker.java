package com.xiaoke1256.orders.common.zookeeper;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

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

public abstract class Worker extends BaseWatcher {
	
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
	
	@PostConstruct
	public void bootstrap() throws InterruptedException {
		register();
	}
	
	public void register() {
		//Create assign node first.
		zooKeeper.create(baseNodePath+"/assign/worker-"+serverId,
				new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createAssignWorkerCallback, null);
	}
	
	private StringCallback createAssignWorkerCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				register();
				break;
			case OK:
				//节点创建成功，则创建worker节点。
				registerWorkNode();
				logger.info("Assign worker node create successfully: %s",serverId);
				break;
			case NODEEXISTS:
				logger.warn("Already registered: %s",serverId );
				break;
			default:
				logger.error("Something went wrong: ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};

	public void registerWorkNode() {
		zooKeeper.create(baseNodePath+"/workers/worker-"+serverId,
				"Idle".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, createWorkerCallback, null);//"Idle" 表示空闲
	}
	
	private StringCallback createWorkerCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				registerWorkNode();
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
	
	private Set<String> onGoingTasks = new HashSet<>();//TODO 此HsahSet 大小应该限制，根据节点的处理能力限制。
	
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
									//TODO 如果onGoingTasks已满，应该直接break.
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
			switch(Code.get(rc)){
			case CONNECTIONLOSS:
				getTaskData((String)ctx);
				break;
			case OK:
				try {
					doBusiness(new String(data));
				}catch(Exception e){
					logger.error("Some errer happen when excute business.",e);
					throw e;
				}finally {
					finishTask((String)ctx);
				}
				break;
			default:
				logger.error("Get TaskData fail: ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};

	private void finishTask(String task) {
	    while(true){
			synchronized (onGoingTasks) {
				String path = baseNodePath+"/status/"+task;
				String content = "done by worker-"+serverId;
				try {
					zooKeeper.setData(path, content.getBytes(), -1);
					onGoingTasks.remove(task);
					return;
				} catch (KeeperException e) {
					switch (e.code()) {
					case NONODE:
						//节点没有应该是其他其他线程把它删了.
						logger.warn("The node has bean deleted by the master.");
						return;
					case CONNECTIONLOSS:
						if(ckeckStatusData(path,content)) {
							logger.info("Modify successfull!");
							onGoingTasks.remove(task);
							return;
						}
					default:
						//其他异常。
						logger.error("Something wrong has happen!",e);
					}
				} catch (InterruptedException e) {
					logger.error("Suspend!",e);
					
				}
				
			}
	    }
	}
	
	private boolean ckeckStatusData(String path, String content) {
		while(true) {
			try {
				Stat stat = new Stat();
				byte[] data = zooKeeper.getData(path, false,stat);
				return new String(data).equals(content);
			}catch(KeeperException e) {
				switch (e.code()) {
				case NONODE:
					//节点没有应该是其他其他线程把它删了.说明也修改成功了。
					logger.warn("The node has bean deleted by the master.");
					return true;
				default:
					//其他异常（含ConnectLossException）. Read it again.
					logger.error("Something wrong has happen!",e);
				}
			} catch (InterruptedException e) {
				logger.error("Something wrong has happen!",e);
				//继续查状态
			}
		}
	}
	
	/**
	 * 处理具体业务
	 * @param data
	 */
	abstract protected void doBusiness(String businessData) ;
	
}
