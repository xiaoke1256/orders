package com.xiaoke1256.orders.search.task;

import java.util.Random;

import org.apache.zookeeper.AsyncCallback.StringCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooDefs.Ids;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.search.common.zookeeper.BaseWatcher;

@Component
public class ZookeeperWatcher extends BaseWatcher {
	private volatile Boolean isMaster;
	
	private String serverId = Integer.toHexString(new Random().nextInt());
	
	public void toBeMast(String nodePath) {
		this.zooKeeper.create(nodePath, serverId.getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, cb  , nodePath);
	}
	
	/**
	 * 反馈函数
	 */
	private StringCallback cb = (int rc, String path, Object ctx, String name) -> {
		System.out.println("rc"+rc);
		System.out.println("Code.get(rc)"+Code.get(rc));
		switch(Code.get(rc)) {
		case CONNECTIONLOSS:
			//重新发起并竞选群首。
			toBeMast(ctx.toString());
			break;
		case OK:
			//我是群首
			isMaster = Boolean.TRUE;
			break;
		default:
			//竞选失败
			isMaster = Boolean.FALSE;
		}
		if(isMaster)
			System.out.println("I'm master.");
		else
			System.out.println("I'm not master.");
	};


}
