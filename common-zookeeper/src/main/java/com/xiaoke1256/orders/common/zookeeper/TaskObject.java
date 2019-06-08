package com.xiaoke1256.orders.common.zookeeper;

public class TaskObject {
	private String taskName;//task在zk上的节点名称。
	private String task;//task的业务ID
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	
}
