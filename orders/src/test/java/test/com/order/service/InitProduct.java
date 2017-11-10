package test.com.order.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.bo.Product;
import com.xiaoke1256.orders.dao.BaseDao;
import com.xiaoke1256.orders.service.OrederService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"/config/applicationContext.xml"
		})
public class InitProduct {
	
	@Before
	public void init(){
		//log4j初始化
        String basePath  = Class.class.getResource("/").getPath();
        basePath = basePath.substring(0,basePath.indexOf("/classes"));
        String log4jPath = basePath+"/classes/"+"log4j-test.properties";
        //BasicConfigurator.configure ();
        PropertyConfigurator.configure (log4jPath) ;
	}
	
	@Autowired
	private BaseDao baseDao;
	//@Test
	@Rollback(false)
	@Transactional
	public void initProduct(){
		Random r = new Random();
		
		System.out.print("start time:"+System.currentTimeMillis());
		
		//10000件商品
		for(int i=0;i<10000;i++){
			 Product p = new Product();
			 p.setProductId(String.valueOf(i));
			 p.setProductPrice(BigDecimal.valueOf(Math.round(r.nextInt(100*100))).divide(BigDecimal.valueOf(100)));
			 Long stockNum = Long.valueOf(r.nextInt(1000));
			 while(stockNum==0)
				 stockNum = Long.valueOf(r.nextInt(1000));
			 p.setStockNum(stockNum);
			 p.setStoreNo(String.valueOf(r.nextInt(100)));
			 p.setInsertTime(new Timestamp(System.currentTimeMillis()));
			 p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			 baseDao.persist(p);
		}
		
		System.out.print("end time:"+System.currentTimeMillis());
	}
	
	/**
	 * 补货
	 */
	@Test
	@Rollback(false)
	@Transactional
	public void restoreProduct(){
		Random r = new Random();
		List<Product> products = (List<Product>) baseDao.queryByHql("from Product where stockNum <= 200 ");
		for(Product product:products){
			Long stockNum = Long.valueOf(r.nextInt(1000));
			 while(stockNum<=500)
				 stockNum = Long.valueOf(r.nextInt(1000));
			product.setStockNum(stockNum);
			product.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			baseDao.merge(product);
		}
	}
}
