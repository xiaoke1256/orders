package test.com.xiaoke1256.orders.product.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.SpringbootApplication;
import com.xiaoke1256.orders.product.bo.ProductType;
import com.xiaoke1256.orders.product.dao.ProductTypeDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringbootApplication.class)
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
