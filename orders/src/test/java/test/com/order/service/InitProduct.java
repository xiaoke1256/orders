package test.com.order.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"/config/applicationContext.xml"
		})
public class InitProduct {
	
	@Before
	public void init(){
		//log4j鍒濆鍖�
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
		
		//10000浠跺晢鍝�
		for(int i=0;i<10000;i++){
			 Product p = new Product();
			 p.setProductCode(StringUtils.leftPad(String.valueOf(i), 10, '0'));
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
	 * 琛ヨ揣
	 */
	@Test
	@Rollback(false)
	@Transactional
	public void restoreProduct(){
		Random r = new Random();
		@SuppressWarnings("unchecked")
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
