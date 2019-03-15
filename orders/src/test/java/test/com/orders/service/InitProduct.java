package test.com.orders.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

//import org.apache.logging.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.SpringbootApplication;
import com.xiaoke1256.orders.common.dao.BaseDao;
import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.product.dto.Product;
import com.xiaoke1256.orders.product.dto.ProductQueryResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringbootApplication.class)
@ActiveProfiles("test")
public class InitProduct {
	
	@Value("${remote.api.product.uri}")
	private String productApiUri;
	
	@Before
	public void init(){
		//log4j初始化
//        String basePath  = Class.class.getResource("/").getPath();
//        String log4jPath = "";
//        if(basePath.indexOf("test-classes")>0) {
//	        basePath = basePath.substring(0,basePath.indexOf("/test-classes"));
//	        log4jPath = basePath+"/test-classes/"+"log4j-test.properties";
//        }else {
//        	basePath = basePath.substring(0,basePath.indexOf("/classes"));
//        	log4jPath = basePath+"/test-classes/"+"log4j-test.properties";
//        }
//        //BasicConfigurator.configure ();
//        PropertyConfigurator.configure (log4jPath) ;
	}
	
	@Autowired
	private BaseDao baseDao;
	
	@Resource
	private RestTemplate restTemplate;
	
	@Test
	@Transactional
	@Rollback(false)
	public void initOStorage() {
		ProductQueryResult result = restTemplate.getForObject(productApiUri+"/product/search?pageSize="+Integer.MAX_VALUE, ProductQueryResult.class);
		for(Product product:result.getResultList()) {
			String hql = "from OStorage o where o.productCode='"+product.getProductCode()+"'";
			@SuppressWarnings("unchecked")
			List<OStorage> oList = (List<OStorage>)baseDao.queryByHql(hql);
			if(oList.size()>0)
				continue;
			OStorage storage = new OStorage();
			storage.setProductCode(product.getProductCode());
			storage.setStockNum(0l);
			Timestamp now = new Timestamp(System.currentTimeMillis());
			storage.setInsertTime(now);
			storage.setUpdateTime(now);
			baseDao.persist(storage);
		}
	}
	
	/**
	 * 补货
	 */
	@Test
	@Rollback(false)
	@Transactional
	public void restoreOStorage(){
		Random r = new Random();
		@SuppressWarnings("unchecked")
		List<OStorage> products = (List<OStorage>) baseDao.queryByHql("from OStorage where stockNum <= 200 ");
		for(OStorage product:products){
			Long stockNum = Long.valueOf(r.nextInt(1000));
			 while(stockNum<=500)
				 stockNum = Long.valueOf(r.nextInt(1000));
			product.setStockNum(stockNum);
			product.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			baseDao.merge(product);
		}
	}
}
