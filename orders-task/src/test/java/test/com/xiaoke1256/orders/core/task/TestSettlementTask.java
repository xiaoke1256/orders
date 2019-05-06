package test.com.xiaoke1256.orders.core.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiaoke1256.orders.SpringbootApplication;
import com.xiaoke1256.orders.core.task.SettlementTask;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SpringbootApplication.class)
@ActiveProfiles("test")
public class TestSettlementTask {
	@Autowired
	private SettlementTask settlementTask;
	
	@Test
	public void testSettlementTask() {
		settlementTask.startSettlement();
	}
}
