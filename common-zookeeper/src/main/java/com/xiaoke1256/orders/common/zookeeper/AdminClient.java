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

	@Override
	protected void reboot() {
		//do nothing.
	}
	
	

}
