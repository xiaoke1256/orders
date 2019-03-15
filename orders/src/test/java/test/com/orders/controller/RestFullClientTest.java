package test.com.orders.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.core.controller.OrderPlaceRequest;
import com.xiaoke1256.orders.core.controller.OrderPlaceResponse;

public class RestFullClientTest {
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	private static ExecutorService pool = Executors.newFixedThreadPool(100);
	
	public static void main(String[] args) throws Exception{
//		System.out.println("start time :"+System.currentTimeMillis());
//		OrderPlaceRequest request = new OrderPlaceRequest();
//		request.setCarriageAmt(new BigDecimal(13.4));
//		request.setPayerNo("322432342432");
//		Map<String, Integer> productMap = new HashMap<String,Integer>();
//		productMap.put("243", 2);
//		productMap.put("246", 3);
//		request.setProductMap(productMap );
//		PayOrder orderVo = restTemplate.postForObject("http://localhost:8080/order_trans_test/orders/", request, PayOrder.class);
//		System.out.println("orderVo:"+orderVo);
//		System.out.println("end time :"+System.currentTimeMillis());
		
		final Random r = new Random();
		final List<StatResult> ressults = new Vector<StatResult>();
//		List<Thread> taskList = new ArrayList<Thread>();
		
		Runnable runnable = ()->{
			StatResult ressult = new StatResult();
			ressult.startTime = System.currentTimeMillis();
			String payerNo = StringUtils.leftPad(String.valueOf(r.nextInt(1000000)),24,"0");
			BigDecimal carriageAmt = BigDecimal.valueOf(4.0);
			Map<String, Integer> productMap = new HashMap<String, Integer>();
			String productCode = StringUtils.leftPad(String.valueOf(r.nextInt(1000)), 10, '0');
			Integer num = r.nextInt(5);
			if(num==0)
				num = 1;
			productMap.put(productCode, num);
			try{
				OrderPlaceRequest request = new OrderPlaceRequest();
				request.setPayerNo(payerNo);
				request.setCarriageAmt(carriageAmt);
				request.setProductMap(productMap);
				OrderPlaceResponse response = restTemplate.postForObject("http://localhost:8080/orders/orders/", request, OrderPlaceResponse.class);
				//System.out.println("order no :"+response.getPayOrderNo());
				if(response.getErrMsg()!=null) {
					System.out.println("excpetion :"+response.getErrMsg().getMsg());
					ressult.isError=true;
				}
			}catch(Exception ex){
				ex.printStackTrace();
				ressult.isError=true;
			}
			ressult.endTime = System.currentTimeMillis();
			ressults.add(ressult);
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
	
	static class StatResult{
		long startTime;
		long endTime;
		boolean isError=false;
	}
}
