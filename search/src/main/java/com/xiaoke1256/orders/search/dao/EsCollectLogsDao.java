package com.xiaoke1256.orders.search.dao;

import java.sql.Timestamp;

import com.xiaoke1256.orders.search.bo.EsCollectLogs;

public interface EsCollectLogsDao {
	public EsCollectLogs getLogById(Long logId);
	
	public Timestamp getLastExeTime();
	
	public void save(EsCollectLogs logs);
	
	public void delete(Long logId);
}
