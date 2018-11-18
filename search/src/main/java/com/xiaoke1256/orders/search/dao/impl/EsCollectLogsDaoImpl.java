package com.xiaoke1256.orders.search.dao.impl;

import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.xiaoke1256.orders.search.bo.EsCollectLogs;
import com.xiaoke1256.orders.search.dao.EsCollectLogsDao;

@Repository
public class EsCollectLogsDaoImpl extends BaseDaoImpl implements EsCollectLogsDao  {

	@Override
	public EsCollectLogs getLogById(Long logId) {
		EsCollectLogs log = this.sqlSessionTemplate
				.selectOne("com.xiaoke1256.orders.search.dao.ProductDao.getLogById", logId);
		return log;
	}

	@Override
	public Timestamp getLastExeTime() {
		Timestamp time = sqlSessionTemplate
				.selectOne("com.xiaoke1256.orders.search.dao.EsCollectLogsDao.getLastExeTime");
		return time;
	}

	@Override
	public void save(EsCollectLogs logs) {
		int result = sqlSessionTemplate.insert("com.xiaoke1256.orders.search.dao.EsCollectLogsDao.save", logs);
		if(result==0)
			throw new RuntimeException("Can not insert.");
	}

	@Override
	public void delete(Long logId) {
		sqlSessionTemplate.delete("com.xiaoke1256.orders.search.dao.EsCollectLogsDao.delete", logId);
	}

}
