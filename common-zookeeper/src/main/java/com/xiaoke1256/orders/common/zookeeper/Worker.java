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
			try {
				switch(Code.get(rc)) {
				case CONNECTIONLOSS:
					Thread.sleep(300);
					logger.warn("Connection loss !");
					register();
					break;
				case NONODE:
					Thread.sleep(300);
					logger.warn("Parent Node has not been created !");
					register();//父节点还没有被创建
					break;
				case OK:
					//节点创建成功，则创建worker节点。
					registerWorkNode();
					logger.info("Assign worker node create successfully: {}",serverId);
					break;
				case NODEEXISTS:
					logger.warn("Already registered: {}",serverId );
					registerWorkNode();
					break;
				default:
					logger.error("Something went wrong: ",KeeperException.create(Code.get(rc),path));
				}
			}catch( InterruptedException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e);
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
			try {
				switch(Code.get(rc)) {
				case CONNECTIONLOSS:
					logger.warn("Connection loss !");
					Thread.sleep(300);
					registerWorkNode();
					break;
				case NONODE:
					logger.warn("Parent node has not created !");
					Thread.sleep(300);
					registerWorkNode();//父节点还没有被创建
					break;
				case OK:
					//节点创建成功,开始监控任务
					getTasks();
					logger.info("Regitered successfully: %s",serverId);
					break;
				case NODEEXISTS:
					logger.warn("Already registered: %s",serverId );
					break;
				default:
					logger.error("Something went wrong: ",KeeperException.create(Code.get(rc),path));
				}
			}catch( InterruptedException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e);
			}
			
		}
		
	};
	
	private StatCallback statusUpdateCallback = new StatCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			try {
				switch(Code.get(rc)) {
				case CONNECTIONLOSS:
					Thread.sleep(300);
					logger.warn("Connection loss !");
					updateStatus((String)ctx);
					return;
				default:
					break;
				}
			}catch( InterruptedException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e);
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
	
	private Set<String> onGoingTasks = new HashSet<>();//TODO 此HsahSet 大小应该限制，根据服务器的处理能力限制。
	
	private ChildrenCallback tasksGetChildrenCallback = new ChildrenCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children) {
			try {
				switch(Code.get(rc)) {
				case CONNECTIONLOSS:
					Thread.sleep(300);
					logger.warn("Connection loss !");
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
			}catch( InterruptedException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e);
			}
		}
		
	};
	
	private void getTaskData(String task) {
		zooKeeper.getData(baseNodePath+"/assign/worker-"+serverId+"/"+task, false, excuteTaskCallback,task);
	}
	
	private DataCallback excuteTaskCallback = new DataCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			try {
				switch(Code.get(rc)){
				case CONNECTIONLOSS:
					Thread.sleep(300);
					logger.warn("Connection loss !");
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
			}catch( InterruptedException e) {
				logger.error(e.getMessage(),e);
				throw new RuntimeException(e);
			}
		}
		
	};

	private void finishTask(String task) {
	    while(true){
			synchronized (onGoingTasks) {
				String path = baseNodePath+"/status/"+task;
				String content = "done by worker-"+serverId;
				try {
					zooKeeper.create(path, content.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					onGoingTasks.remove(task);
					return;
				} catch (KeeperException e) {
					switch (e.code()) {
					case NONODE:
						//节点没有应该是其他其他线程把它删了.
						logger.warn("The node has bean deleted by the master.");
						onGoingTasks.remove(task);
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
				try {
					Thread.sleep(300);
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
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				logger.error("Suspend!",e);
			}
		}
	}
	
	/**
	 * 处理具体业务
	 * @param data
	 */
	abstract protected void doBusiness(String businessData) ;

	@Override
	protected void reboot() throws InterruptedException {
		register();
	}
	
	
	
}
