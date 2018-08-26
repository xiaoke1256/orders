package test.com.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xiaoke1256.orders.service.OrederService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"/config/applicationContext.xml"
		})
public class OrederServiceTest {
	private ExecutorService pool = Executors.newFixedThreadPool(100);
	
	@Before
	public void init(){
//		//log4j初始化
//        String basePath  = Class.class.getResource("/").getPath();
//        String log4jPath = "";
//        if(basePath.indexOf("test-classes")>0) {
//	        basePath = basePath.substring(0,basePath.indexOf("/test-classes"));
//	        log4jPath = basePath+"/test-classes/"+"log4j-test.properties";
//        }else {
//        	basePath = basePath.substring(0,basePath.indexOf("/classes"));
//        	log4jPath = basePath+"/test-classes/"+"log4j-test.properties";
//        }
//        //BasicConfigurator.configure ();
//        PropertyConfigurator.configure (log4jPath) ;
	}
	
	@Resource
	private OrederService orederService;
	
	@Test
	public void testPlaceOrder(){
		Random r = new Random();
		
		String payerNo = "test18";
		BigDecimal carriageAmt = BigDecimal.valueOf(4.0);
		Map<String, Integer> productMap = new HashMap<String, Integer>();
		String productId = String.valueOf(r.nextInt(1000));
		Integer num = r.nextInt(5);
		if(num==0)
			num = 1;
		productMap.put(productId, num);
		orederService.place(payerNo , carriageAmt , productMap );
	}
	
	//@Test
	public void testConcurrentPlaceOrder() throws Exception{
		final Random r = new Random();
		final List<StatResult> ressults = new Vector<StatResult>();
//		List<Thread> taskList = new ArrayList<Thread>();
		
		Runnable runnable = new Runnable(){
			public void run(){
				StatResult ressult = new StatResult();
				ressult.startTime = System.currentTimeMillis();
				String payerNo = StringUtils.leftPad(String.valueOf(r.nextInt(1000000)),24,"0");
				BigDecimal carriageAmt = BigDecimal.valueOf(4.0);
				Map<String, Integer> productMap = new HashMap<String, Integer>();
				String productId = String.valueOf(r.nextInt(1000));
				Integer num = r.nextInt(5);
				if(num==0)
					num = 1;
				productMap.put(productId, num);
				try{
					orederService.place(payerNo , carriageAmt , productMap );
				}catch(Exception ex){
					ex.printStackTrace();
					ressult.isError=true;
				}
				ressult.endTime = System.currentTimeMillis();
				ressults.add(ressult);
			}
		};
		long totalStart = System.currentTimeMillis();
		for(int i=0;i<1000;i++){
			for(int j=0;j<10;j++){
//				Thread taske = new Thread(runnable);
//				taskList.add(taske);
//				taske.start();
				pool.execute(runnable);
			}
			
			Thread.sleep(1);
		}
		
		//等待线程执行完毕
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.HOURS);
//		for(Thread task:taskList){
//			task.join();
//		}
		long totalEnd = System.currentTimeMillis();
		//计算
		long sumTime = 0;
		long errorNum = 0;
		for(StatResult ressult:ressults){
			sumTime+= (ressult.endTime - ressult.startTime);
			if(ressult.isError)
			errorNum++;
		}
		long totalTime = totalEnd - totalStart;
		System.out.println("error_num:"+errorNum);
		System.out.println("sum_time:"+(sumTime)+"ms");
		System.out.println("total_time:"+(totalTime)+"ms");
		System.out.println("avg_time:"+((double)sumTime/ressults.size())+"ms");
		System.out.println("tps:"+(ressults.size()*ressults.size()/((double)sumTime*.001*totalTime*.001)));
	}
	
	class StatResult{
		long startTime;
		long endTime;
		boolean isError=false;
	}
}
