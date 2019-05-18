package com.xiaoke1256.orders.common.zookeeper;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.AsyncCallback.DataCallback;
import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminClient extends BaseWatcher {
	private static final Logger logger = LoggerFactory.getLogger(AdminClient.class);
	
	private String baseNodePath;
	
	public void listState() throws KeeperException,InterruptedException {
//		try {
//			Stat stat = new Stat();
//			byte[] masterData = zooKeeper.getData(baseNodePath+"/master", false, stat );
//			Date startDate = new Date(stat.getCtime());
//		} catch (NoNodeException e) {
//			
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
		zooKeeper.getData(baseNodePath+"/tasks", false, taskDataCallback, task);
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
				int workerIndex = RandomUtils.nextInt(workersCase.size());
				String designatedWorker = workersCase.get(workerIndex);
				
				String assignmentPath = baseNodePath+"/assign/"+designatedWorker+"/"+(String)ctx;
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

	protected void deleteTask(String substring) {
		// TODO Auto-generated method stub
		
	}

}
