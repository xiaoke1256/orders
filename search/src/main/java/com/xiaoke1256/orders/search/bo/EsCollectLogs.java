package com.xiaoke1256.orders.search.bo;

import java.io.Serializable;
import java.sql.Timestamp;

public class EsCollectLogs implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long logId;
	private Integer modifyCount;
	private Integer newCount;
	private Integer offlineCount;
	private Timestamp exeTime;
	private Timestamp insertTime;
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public Integer getModifyCount() {
		return modifyCount;
	}
	public void setModifyCount(Integer modifyCount) {
		this.modifyCount = modifyCount;
	}
	public Integer getNewCount() {
		return newCount;
	}
	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}
	public Integer getOfflineCount() {
		return offlineCount;
	}
	public void setOfflineCount(Integer offlineCount) {
		this.offlineCount = offlineCount;
	}
	public Timestamp getExeTime() {
		return exeTime;
	}
	public void setExeTime(Timestamp exeTime) {
		this.exeTime = exeTime;
	}
	public Timestamp getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}
	
}
