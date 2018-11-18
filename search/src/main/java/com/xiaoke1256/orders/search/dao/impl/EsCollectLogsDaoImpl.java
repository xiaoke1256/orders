package com.xiaoke1256.orders.search.dao.impl;

import java.sql.Timestamp;

import com.xiaoke1256.orders.search.bo.EsCollectLogs;
import com.xiaoke1256.orders.search.dao.EsCollectLogsDao;

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

}
