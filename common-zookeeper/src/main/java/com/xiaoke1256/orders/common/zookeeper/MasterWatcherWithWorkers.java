package com.xiaoke1256.orders.common.zookeeper;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Op;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 选举为群主的zookeeper主节点，并且需要从节点配合完成业务。
 * @author Administrator
 *
 */
public class MasterWatcherWithWorkers extends MasterWatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(MasterWatcherWithWorkers.class);

	public MasterWatcherWithWorkers(String nodePath) {
		super(nodePath);
	}
	
	/**
	 * Create meta node.
	 * @throws InterruptedException 
	 */
	protected void bootstrap() throws InterruptedException {
		String basePath = baseNodePath;
		logger.info("basePath:"+basePath);
		if(basePath.lastIndexOf("/master")>=0)
			basePath.substring(0, basePath.lastIndexOf("/master"));
		createParent(basePath+"/workers",new byte[0]);
		createParent(basePath+"/assign",new byte[0]);
		createParent(basePath+"/tasks",new byte[0]);
		createParent(basePath+"/status",new byte[0]);
		//监控/workes节点
		getWorkers();
		//监控/tasks节点
		getTasks();
	}
	
	@PostConstruct
	public void startUp() throws InterruptedException {
		super.toBeMast();
	}

	private void createParent(String path, byte[] data) throws InterruptedException {
		try {
			zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException e) {
			switch (e.code()) {
			case NODEEXISTS:
				logger.info("Path {} already exist .",path);//有可能是以前已经创建好了
				return;
			case CONNECTIONLOSS:
				//检查我还是不是主节点了
				if(this.checkMaster(baseNodePath+"/master")) {
					createParent(path,data);//如果是继续创建
				}else {
					//如果不是了，就抛异常，后续工作都不用做了。
					throw new RuntimeException("I'm not the master.");
				}
				break;
			default:
				//其他异常
				logger.error(e.getMessage(), e);
				throw new RuntimeException(e);
			}
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
		String assignmentPath = baseNodePath+"/assign/"+worker;
		zooKeeper.getChildren(assignmentPath, false, getAbsentWorkerTasksCallBack, worker);
	}
	
	private ChildrenCallback getAbsentWorkerTasksCallBack = (int rc, String path, Object ctx, List<String> tasks)->{
		switch(Code.get(rc)) {
		case CONNECTIONLOSS:
			getAbsentWorkerTasks((String)ctx);
			break;
		case OK:
			if(tasks!=null) {
				reassignTasks((String)ctx,tasks);
			}
			break;
		default:
			logger.error("get children fail: ",KeeperException.create(Code.get(rc),path));
		}
	};
	
	private void reassignTasks(String worker,List<String> tasks) {
		//reassign the taskes.
		for(String task:tasks) {
			reassignTask(worker,task);
		}
		//delete the /assign/worker-{} node.
		deleteAssignWorkNode(worker);
	}
	
	private void reassignTask(String worker,String task){
		while(true) {
			try {
				//完成以下事情： 1、删 除 /assign/work-?/ 下对应节点。创建任务节点
				String assignPath = baseNodePath + "/assign/"+worker+"/"+task;
				String taskesPath = baseNodePath + "/tasks/"+task;
				Stat stat = new Stat();
				byte[] data = zooKeeper.getData(assignPath, false, stat );
				zooKeeper.multi(Arrays.asList(Op.delete(assignPath, -1)
						,Op.create(taskesPath, data, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL)));
				return;
			} catch (KeeperException e) {
				switch (e.code()) {
				case NONODE:
					//说明有其他节点已经帮他处理掉了。
					logger.info("the node has been deleted!");
					return;
				case NODEEXISTS:
					//说明有其他节点已经帮他处理掉了。
					logger.info("the node has been created!");
					return;
				default:
					//其他异常（含ConnectLossException）。
					logger.warn("Something wrong has happen when delete reassign task. ",e);
					//继续循环
				}
			} catch (InterruptedException e) {
				logger.error("Something wrong has happen when delete reassign task. ",e);
				//继续循环
			}
		}
		
	}
	
	private void deleteAssignWorkNode(String worker) {
		String assignmentPath = baseNodePath+"/assign/"+worker;
		while(true) {
			try {
				zooKeeper.delete(assignmentPath, -1);
			}catch (KeeperException e) {
				switch (e.code()) {
				case NONODE:
					//说明有其他节点已经帮他处理掉了。
					logger.info("the node has been deleted!");
					return;
				default:
					//其他异常（含ConnectLossException）。
					logger.warn("Something wrong has happen when delete reassign worker task. ",e);
					//继续循环
				}
			} catch (InterruptedException e) {
				logger.error("Something wrong has happen when delete reassign worker task. ",e);
				//继续循环
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

	protected void getTasks() {
		zooKeeper.getChildren(baseNodePath+"/tasks", taskChangeWatcher,getTasksCallback,null);
	}
	
	private ChildrenCallback getTasksCallback = new ChildrenCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, List<String> tasks) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				getTasks();
				break;
			case OK:
				if(tasks!=null) {
					assignTask(tasks);
				}
				break;
			default:
				logger.error("get children fail: ",KeeperException.create(Code.get(rc),path));
			}
			
		}
		
	};

	protected void assignTask(List<String> tasks) {
		for(String task:tasks) {
			getTaskData(task);
		}
	}

	private void getTaskData(String task) {
		zooKeeper.getData(baseNodePath+"/tasks/"+task, false, taskDataCallback, task);
	}
	
	private DataCallback taskDataCallback = new DataCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				getTaskData((String)ctx);
				break;
			case OK:
				/**
				 * Choose worker at random
				 */
				if(workersCase.size()==0) {
					//工作节点还没有注册上来需稍等片刻
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						logger.error("the task assign  terminated!",e);
					}
					getTaskData((String)ctx);
					break;
				}
				//TODO 优先考虑空闲的节点。
				int workerIndex = RandomUtils.nextInt(workersCase.size());
				String designatedWorker = workersCase.get(workerIndex);
				
				String assignmentPath = baseNodePath+"/assign/"+designatedWorker+"/"+(String)ctx;
				logger.debug("Task data is: "+new String(data));
				createAssignment(assignmentPath,data);
				break;
			default:
				logger.error("Error happen when get task data : ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};

	protected void createAssignment(String assignmentPath, byte[] data) {
		zooKeeper.create(assignmentPath, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, assignTaskCallback, data);
	}
	
	private StringCallback assignTaskCallback = new StringCallback() {

		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				createAssignment(path,(byte[])ctx);
				break;
			case OK:
				logger.info("Task assigned successfull: "+name);
				deleteTask(name.substring(name.lastIndexOf("/")+1));
			case NODEEXISTS:
				logger.warn("Task already assigned");
				break;
			default:
				logger.error("Error happen when try to assign task : ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};

	private void deleteTask(String task) {
		String path = baseNodePath +"/tasks/"+task;
		zooKeeper.delete(path,-1, deleteTaskCallback, task);
	}
	
	private VoidCallback deleteTaskCallback = (int rc, String path, Object ctx)->{
		switch(Code.get(rc)) {
		case CONNECTIONLOSS:
			deleteTask((String)ctx);
			break;
		case OK:
			logger.info("Task deleted successfull: "+(String)ctx);
			break;
		case NONODE:
			logger.warn("Task already deleted");
			break;
		default:
			logger.error("Error happen when try to assign task : ",KeeperException.create(Code.get(rc),path));
		}
	} ;

}
