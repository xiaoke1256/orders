package test.com.xiaoke1256.orders.search.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringbootApplication;
import com.xiaoke1256.orders.search.bo.ProductType;
import com.xiaoke1256.orders.search.dao.ProductTypeDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringbootApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class ProductTypeDaoTest {
	@Resource
	private ProductTypeDao productTypeDao;
	
	@Test
	public void testGetTypesByProductCode(){
		List<ProductType> list = productTypeDao.getTypesByProductCode("0006100404");
		for(ProductType type : list) {
			System.out.println("type name:"+type.getTypeName());
		}
	}

}
