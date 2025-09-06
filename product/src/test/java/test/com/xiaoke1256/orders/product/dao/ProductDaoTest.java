package test.com.xiaoke1256.orders.product.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiaoke1256.orders.ProductApplication;
import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.bo.ProductParam;
import com.xiaoke1256.orders.product.bo.ProductType;
import com.xiaoke1256.orders.product.dao.ProductDao;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= ProductApplication.class)
public class ProductDaoTest {
	
	@Resource
	private ProductDao productDao;
	
	@Test
	public void testGet() {
		Product p = productDao.getProductByCode("0006101504");
		if(p != null) {
			System.out.println("productName : "+ p.getProductName());
			if(p.getParams()!=null)
				for(ProductParam param:p.getParams()) {
					System.out.println("paramName : "+ param.getParamName());
					System.out.println("paramValue : "+ param.getParamValue());
				}
		}else {
			System.out.println("product is null. ");
		}
	}
	
	@Test
	public void testQueryModified() {
		List<Product> l = productDao.queryModifed(null, "0");
		for(Product p:l) {
			System.out.println("productName : "+ p.getProductName());
			System.out.println("store :"+p.getStore().getStoreName());
		}
	}
	
	@Test
	public void testQueryModifiedByTime() {
		@SuppressWarnings("deprecation")
		List<Product> l = productDao.queryModifed(new Timestamp(new Date(2018-1970,1,1).getTime()), "1");
		for(Product p:l) {
			System.out.println("productName : "+ p.getProductName());
			if(p.getStore()!=null)
				System.out.println("store :"+p.getStore().getStoreName());
			else
				System.out.println("store :"+null);
			System.out.print("types :");
			if(p.getProductTypes()!=null)
				for(ProductType type:p.getProductTypes()) {
					System.out.print(type.getTypeName()+",");
				}
			System.out.println();
		}
	}
	
	@Test
	public void testQueryByCondition() {
		ProductCondition condition = new ProductCondition();
		condition.setProductName("米");
		condition.setStoreNos(new String[]{"0001","2018100009"});
		List<Product> l = productDao.queryByCondition(condition );
		if(l.size()>0 && l.size()<10){
			Assert.assertEquals(l.size(),condition.getTotal());
		}else if(l.size()>=10){
			System.out.println("condition.getTotal()="+condition.getTotal());
			Assert.assertTrue(condition.getTotal()>=10);
		}
		for(Product p:l) {
			System.out.println("productName : "+ p.getProductName());
			if(p.getStore()!=null)
				System.out.println("store :"+p.getStore().getStoreName());
			else
				System.out.println("store :"+null);
			System.out.print("types :");
			if(p.getProductTypes()!=null)
				for(ProductType type:p.getProductTypes()) {
					System.out.print(type.getTypeName()+",");
				}
			System.out.println();
		}
		
	}
}
