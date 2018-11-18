package com.xiaoke1256.orders.search.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.search.bo.EsCollectLogs;
import com.xiaoke1256.orders.search.bo.Product;
import com.xiaoke1256.orders.search.dao.EsCollectLogsDao;
import com.xiaoke1256.orders.search.dao.ProductDao;

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
	
	@Autowired
	private ProductDao productDao;
	
	public void collectProduct(){
		//Search in log table(EsCollectLogs), get the latest execute time.
		Timestamp lastExeTime = esCollectLogsDao.getLastExeTime();
		Date now = new Date();
		//Query modified products after last execute time.
		List<Product> onlineList = productDao.queryModifed(lastExeTime, "1");//"1" 为已上线。 
		
		//Insert or update into Es.
		
		//Query disabled product after last execute time.
		List<Product> offlineList = productDao.queryModifed(lastExeTime, "0");//"1" 为已下线。 
		
		//Delete from Es.
		
		//Save logs.
		EsCollectLogs log = new EsCollectLogs();
		log.setExeTime(new Timestamp(now.getTime()));
		log.setInsertTime(new Timestamp(System.currentTimeMillis()));
		log.setModifyCount(onlineList.size());
		log.setNewCount(0);
		log.setOfflineCount(offlineList.size());
		esCollectLogsDao.save(log);
		System.out.println("log id:"+log.getLogId());
	}
}
