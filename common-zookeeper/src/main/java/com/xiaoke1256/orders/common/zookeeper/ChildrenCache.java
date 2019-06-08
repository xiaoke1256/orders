package com.xiaoke1256.orders.common.zookeeper;

import java.util.ArrayList;
import java.util.List;

public class ChildrenCache {
	private List<String> children = new ArrayList<String>();

	public List<String> getChildren() {
		return children;
	}

	public void setChildren(List<String> children) {
		this.children = children;
	}

	public ChildrenCache(List<String> children) {
		super();
		this.children = children;
	}
	
	public int size() {
		return children.size();
	}
	
	public String get(int index) {
		return children.get(index);
	}

	public List<String> removedAndSet(List<String> children) {
		List<String> absentList = new ArrayList<String>();
		if(children==null)
			children = new ArrayList<>();
		List<String> toAddList = new ArrayList<String>();
		for(String child:children) {
			if(!this.children.contains(child)) {
				toAddList.add(child);
			}
		}
		for(String child:this.children) {
			if(!children.contains(child)) {
				absentList.add(child);
			}
		}
		this.children.removeAll(absentList);
		this.children.addAll(toAddList);
		return absentList;
	}
}
