package test.com.orders.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.core.dto.ProductWithStorage;
import com.xiaoke1256.orders.member.dto.Member;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.controller.OrderPlaceRequest;

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

		//获取所有商品
		QueryResultResp<Map<String,Object>> resp = restTemplate.getForObject("http://localhost:8081/products", QueryResultResp.class);
		List<Map<String,Object>> products = resp.getResultList();
		//随机取100个商品
		Random r = new Random();
		for(int i=0;i<100;i++) {
			int index = r.nextInt(products.size());
			System.out.println("products.get(index).getClass():"+products.get(index));
			Map<String,Object> product = products.get(index);
		}
		
		//选取5个用户
		List<Map<String,Object>> members =restTemplate.getForObject("http://localhost:8081/members", List.class);
		List<String> payerNos = new ArrayList<String>();
		for(int i=0;i<5;i++) {
			String accountNo = (String)members.get(i).get("accountNo");
			payerNos.add(accountNo);
		}

		final List<StatResult> ressults = new Vector<StatResult>();
//		List<Thread> taskList = new ArrayList<Thread>();
		
		Runnable runnable = ()->{
			StatResult ressult = new StatResult();
			ressult.startTime = System.currentTimeMillis();
			String payerNo = payerNos.get( r.nextInt(payerNos.size()));
			BigDecimal carriageAmt = BigDecimal.valueOf(4.0);
			Map<String, Integer> productMap = new HashMap<String, Integer>();
			String productCode = (String)products.get(r.nextInt(products.size())).get("productCode");//StringUtils.leftPad(String.valueOf(r.nextInt(1000)), 10, '0');
			Integer num = r.nextInt(5);
			if(num==0)
				num = 1;
			productMap.put(productCode, num);
			try{
				OrderPlaceRequest request = new OrderPlaceRequest();
				request.setPayerNo(payerNo);
				request.setCarriageAmt(carriageAmt);
				request.setProductMap(productMap);
				RespMsg respMsg = restTemplate.postForObject("http://localhost:8081/orders/place", request, RespMsg.class);
				//System.out.println("order no :"+response.getPayOrderNo());
				if(RespMsg.SUCCESS.getCode().equals(respMsg.getCode())) {
					RespMsg errMsg = (RespMsg)respMsg;
					System.out.println("excpetion :"+errMsg.getMsg());
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
//			for(int j=0;j<10;j++){
////				Thread taske = new Thread(runnable);
////				taskList.add(taske);
////				taske.start();
//
//			}
			pool.execute(runnable);

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
