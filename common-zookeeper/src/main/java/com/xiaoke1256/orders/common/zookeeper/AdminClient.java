package com.xiaoke1256.orders.common.zookeeper;

import java.util.Date;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.data.Stat;

public class AdminClient extends BaseWatcher {
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

}
