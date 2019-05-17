package com.xiaoke1256.orders.common.zookeeper;

import java.util.Random;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 选举为群主的zookeeper主键
 * @author Administrator
 *
 */
public class MasterWatcher extends BaseWatcher {
	
	private static final Logger logger = LoggerFactory.getLogger(MasterWatcher.class);
	
	private volatile Boolean isMaster;
	
	private String serverId = Integer.toHexString(new Random().nextInt());
	
	/**zookeeper上节点的路径*/
	private String nodePath;
	
	public MasterWatcher(String nodePath) {
		super();
		this.nodePath = nodePath;
	}

	public boolean toBeMast() throws InterruptedException {
		return toBeMast(false);
	}
	
	/**
	 * 竞选成为主节点
	 * @param needWorker 是否需要从节点
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized boolean toBeMast(boolean needWorker) throws InterruptedException {
		isMaster = null;
		while(true) {
			try {
				this.zooKeeper.create(nodePath, serverId.getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
				isMaster = true;
				bootstrap(needWorker);
				return isMaster;
			} catch (KeeperException e) {
				switch (e.code()) {
				case NODEEXISTS:
					if(checkMaster(nodePath)) {
						bootstrap(isMaster&&needWorker);
						return isMaster;
					}
				default:
					//其他异常（含ConnectLossException）。
					if(checkMaster(nodePath)) {
						bootstrap(isMaster&&needWorker);
						return isMaster;
					}
				}
			}
		}
	}
	
	/**
	 * Check if the note has created.
	 * @param nodePath
	 * @return success or not
	 * @throws InterruptedException
	 */
	private boolean checkMaster(String nodePath) throws InterruptedException {
		while(true) {
			try {
				Stat stat = new Stat();
				byte[] data = zooKeeper.getData(nodePath, false, stat);
				isMaster = new String(data).equals(serverId);
				return true;
			} catch (KeeperException e) {
				switch (e.code()) {
				case NONODE:
					return false;
				default:
					//其他异常（含ConnectLossException）. Read it again.TODO 需打日志。
				}
			}
		}	
	}
	
	/**
	 * crate meta node
	 */
	private void bootstrap(boolean needWorker) {
		if(!needWorker)
			return;
		String basePath = nodePath;
		if(basePath.lastIndexOf("/master")>=0)
			basePath.substring(0, basePath.lastIndexOf("/master"));
		createParent(basePath+"/workers",new byte[0]);
		createParent(basePath+"/assign",new byte[0]);
		createParent(basePath+"/tasks",new byte[0]);
		createParent(basePath+"/status",new byte[0]);
		//TODO 如果不是主节点就要设置监视点
	}
	
	private void createParent(String path, byte[] data) {
		zooKeeper.create(path, data, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, createParent, data);
	}
	
	private StringCallback createParent = new StringCallback() {
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			switch(Code.get(rc)) {
			case CONNECTIONLOSS:
				createParent(path,(byte[])ctx);
				break;
			case OK:
				logger.info("Parent created");
				break;
			case NODEEXISTS:
				logger.warn("Parent already rigistered: %s",path);
				break;
			default:
				logger.error("Something went wrong: ",KeeperException.create(Code.get(rc),path));
			}
		}
		
	};
}
