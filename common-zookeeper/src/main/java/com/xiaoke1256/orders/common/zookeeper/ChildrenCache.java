package com.xiaoke1256.orders.common.zookeeper;

import java.util.ArrayList;
import java.util.List;

public class ChildrenCache {
	List<String> children = new ArrayList<String>();
	
	public ChildrenCache(List<String> children) {
		super();
		this.children = children;
	}

	public List<String> removedAndSet(List<String> children) {
		List<String> absentList = new ArrayList<String>();
		if(children==null || children.size()==0)
			return absentList;
		List<String> toAddList = new ArrayList<String>();
		for(String child:children) {
			if(!this.children.contains(child)) {
				absentList.add(child);
			}
		}
		for(String child:this.children) {
			if(!children.contains(child)) {
				toAddList.add(child);
			}
		}
		this.children.removeAll(absentList);
		this.children.addAll(toAddList);
		return absentList;
	}
}
