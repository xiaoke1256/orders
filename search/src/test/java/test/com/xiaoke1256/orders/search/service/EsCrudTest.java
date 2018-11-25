package test.com.xiaoke1256.orders.search.service;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringApplication;
import com.xiaoke1256.orders.search.bo.Product;
import com.xiaoke1256.orders.search.service.EsCollectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class EsCrudTest {
	@Resource
	private EsCollectService esCollectService;
	
	@Test
	public void testCrud() {
		Product product = new Product();
		product.setProductCode("testtest");
		product.setProductName("测试商品");
		product.setProductIntro("商品简介");
		product.setBrand("商标");
		product.setStoreNo("000032");
		product.setInsertTime(new Timestamp(System.currentTimeMillis()));
		product.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		if(!esCollectService.isExist(product.getProductCode())) {
			esCollectService.index(product );
			System.out.println("商品已创建。");
		}else {
			esCollectService.update(product);
			System.out.println("商品已跟新。");
		}
		
		product.setProductName("测试商品22");
		esCollectService.update(product);	
		System.out.println("商品已跟新。");
		
		esCollectService.delete(product.getProductCode());
		System.out.println("商品已删除。");
	}
}
