package test.com.xiaoke1256.orders.search.service;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringbootApplication;
import com.xiaoke1256.orders.search.service.EsCollectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringbootApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class EsCollectServiceTest {
	
	@Resource
	private EsCollectService esCollectService;
	
	@Test
	public void doCollect() {
		esCollectService.collectProduct();
	}
}
