package test.com.xiaoke1256.orders.search.dao;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.xiaoke1256.orders.search.SpringApplication;
import com.xiaoke1256.orders.search.bo.EsCollectLogs;
import com.xiaoke1256.orders.search.dao.EsCollectLogsDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class EsCollectLogsDaoTest {
	@Resource
	private EsCollectLogsDao esCollectLogsDao;
	
	@Test
	public void testSaveAndDelete() {
		EsCollectLogs log = new EsCollectLogs();
		log.setExeTime(new Timestamp(System.currentTimeMillis()));
		log.setInsertTime(new Timestamp(System.currentTimeMillis()));
		log.setModifyCount(133);
		log.setNewCount(1);
		log.setOfflineCount(3);
		esCollectLogsDao.save(log);
		System.out.println("log id :" +log.getLogId());
		esCollectLogsDao.delete(log.getLogId());
	}
}
