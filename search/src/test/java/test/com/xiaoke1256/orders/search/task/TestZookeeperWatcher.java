package test.com.xiaoke1256.orders.search.task;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringApplication;
import com.xiaoke1256.orders.search.task.ZookeeperWatcher;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class TestZookeeperWatcher {
	@Resource
	private ZookeeperWatcher zookeeperWatcher;
	
	@Test
	public void testToBeMaster() {
		try {
			zookeeperWatcher.toBeMast();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
}
