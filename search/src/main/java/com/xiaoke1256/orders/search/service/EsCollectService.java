package com.xiaoke1256.orders.search.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 搜索引擎， 收集商品的变化
 * @author Administrator
 *
 */
@Service
@Transactional
public class EsCollectService {
	public void collectProduct(){
		//Search in log table(EsCollectLogs), get the latest execute time.
		Date now = new Date();
		//Query the modified products after last execute time.
		
		//Insert into Es.
		
		//Save logs.
	}
}
