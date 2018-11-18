package com.xiaoke1256.orders.search.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.search.dao.EsCollectLogsDao;

/**
 * 搜索引擎， 收集商品的变化
 * @author Administrator
 *
 */
@Service
@Transactional
public class EsCollectService {
	@Autowired
	private EsCollectLogsDao esCollectLogsDao;
	
	public void collectProduct(){
		//Search in log table(EsCollectLogs), get the latest execute time.
		Timestamp lastExeTime = esCollectLogsDao.getLastExeTime();
		Date now = new Date();
		//Query modified products after last execute time.
		
		//Insert or update into Es.
		
		//Query disabled product after last execute time.
		
		//Delete from Es.
		
		//Save logs.
	}
}
