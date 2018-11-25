package com.xiaoke1256.orders.search.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
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
	private Client client;
	
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
		int modifyCount = 0;
		for(Product product:onlineList) {
			if(!isExist(product.getProductCode())) {
				index(product);
			}else {
				update(product);
				modifyCount++;
			}
		}
		
		//Query disabled product after last execute time.
		List<Product> offlineList = productDao.queryModifed(lastExeTime, "0");//"0" 为已下线。 
		
		//Delete from Es.
		for(Product product:offlineList) {
			if(isExist(product.getProductCode())) {
				delete(product.getProductCode());
			}
		}
		//Save logs.
		EsCollectLogs log = new EsCollectLogs();
		log.setExeTime(new Timestamp(now.getTime()));
		log.setInsertTime(new Timestamp(System.currentTimeMillis()));
		log.setModifyCount(modifyCount);
		log.setNewCount(onlineList.size()-modifyCount);
		log.setOfflineCount(offlineList.size());
		esCollectLogsDao.save(log);
		System.out.println("log id:"+log.getLogId());
	}
	
	
	public boolean isExist(String productCode) {
		GetResponse resp = client.prepareGet("prod", "product", productCode)
			.setFetchSource(new String[]{"code"}, null).get();
		return resp.isExists();
	}
	
	public void index(Product product) {
		try {
			Map<String,Object> source = new HashMap<String,Object>();
			source.put("code", product.getProductCode());
			source.put("name", product.getProductName());
			source.put("price", product.getProductPrice().doubleValue());
			source.put("store_no", product.getStoreNo());
			//jsonStr.put("store_name", value)
			//jsonStr.put("type_id", product.get)
			source.put("upd_time", product.getUpdateTime().getTime());
			
			IndexResponse resp = client.index(new IndexRequest("prod", "product",product.getProductCode()).source(source )).get();
			if(resp.getId()==null)
				throw new RuntimeException("Something is wrong!");
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public void update(Product product) {
		try {
			Map<String,Object> source = new HashMap<String,Object>();
			source.put("code", product.getProductCode());
			source.put("name", product.getProductName());
			source.put("price", product.getProductPrice().doubleValue());
			source.put("store_no", product.getStoreNo());
			//jsonStr.put("store_name", value)
			//jsonStr.put("type_id", product.get)
			source.put("upd_time", product.getUpdateTime().getTime());
			UpdateResponse resp = client.update(new UpdateRequest("prod", "product",product.getProductCode()).doc(source )).get();
			if(!Result.UPDATED.equals(resp.getResult())) {
				throw new RuntimeException("Something is wrong!Result is :"+resp.getResult());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(String productCode) {
		try {
			DeleteResponse resp = client.delete(new DeleteRequest("prod", "product",productCode)).get();
			if(Result.NOT_FOUND.equals(resp.getResult())) {
				//warning !!
				return;
			}
			if(!Result.DELETED.equals(resp.getResult())) {
				throw new RuntimeException("Something is wrong!Result is :"+resp.getResult());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}