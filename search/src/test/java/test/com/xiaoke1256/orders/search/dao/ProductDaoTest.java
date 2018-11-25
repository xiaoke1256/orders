package test.com.xiaoke1256.orders.search.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringApplication;
import com.xiaoke1256.orders.search.bo.Product;
import com.xiaoke1256.orders.search.dao.ProductDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ProductDaoTest {
	
	@Resource
	private ProductDao productDao;
	
	@Test
	public void testGet() {
		Product p = productDao.getProductByCode("0006100404");
		System.out.println(p);
		if(p != null) {
			System.out.println("productName : "+ p.getProductName());
		}
	}
	
	@Test
	public void testQueryModified() {
		List<Product> l = productDao.queryModifed(null, "0");
		for(Product p:l) {
			System.out.println("productName : "+ p.getProductName());
		}
	}
	
	@Test
	public void testQueryModifiedByTime() {
		@SuppressWarnings("deprecation")
		List<Product> l = productDao.queryModifed(new Timestamp(new Date(2018-1970,1,1).getTime()), "0");
		for(Product p:l) {
			System.out.println("productName : "+ p.getProductName());
		}
	}
}
