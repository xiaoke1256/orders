package test.com.xiaoke1256.orders.product.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiaoke1256.orders.ProductApplication;
import com.xiaoke1256.orders.product.bo.ProductParam;
import com.xiaoke1256.orders.product.dao.ProductParamDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= ProductApplication.class)
@ActiveProfiles("test")
public class ProductParamDaoTest {
	@Resource
	private ProductParamDao productParamDao;
	
	@Test
	public void testGet() {
		ProductParam p = productParamDao.getById(19l);
		System.out.println(p);
	}
	
	@Test
	public void testGetByProductCode() {
		List<ProductParam> list = productParamDao.getByProductCode("0008100407");
		System.out.println(list.size());
	}
}
