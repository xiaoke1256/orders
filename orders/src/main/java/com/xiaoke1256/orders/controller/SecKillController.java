package com.xiaoke1256.orders.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.common.utils.ResponseUtils;
import com.xiaoke1256.orders.bo.PayOrder;
import com.xiaoke1256.orders.bo.Product;
import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.service.OrederService;
import com.xiaoke1256.orders.service.ProductService;
import com.xiaoke1256.orders.utils.RedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 实现秒杀业务
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/secKill")
public class SecKillController {
	private static  final Logger logger = Logger.getLogger(SecKillController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrederService orederService;
	
	/**
	 * 下订单（利用redis缓存）
	 */
	@RequestMapping(value="/",method={RequestMethod.POST})
	public @ResponseBody OrderPlaceResponse placeOrder(@RequestBody OrderPlaceRequest request) {
		if(request.getProductMap().isEmpty()) {
			throw new RuntimeException("空订单！");
		}
		
		Jedis conn = RedisUtils.connect();
		try {
			List<String> keys = new ArrayList<String>(); //request.getProductMap().keySet().toArray(new String[request.getProductMap().size()]);
			for(Iterator<String> iter=request.getProductMap().keySet().iterator();iter.hasNext();) {
				String productCode = iter.next();
				String key = "SecKill_P_"+productCode;
				String inStore = RedisUtils.get(conn, key);
				if(inStore==null) {
					 productService.openSecKill(productCode);
				}
				keys.add(key);
			}
			
			boolean success = false;
			int retryTimes = 10;//重试次数
			while(!success && retryTimes>0) {
				RedisUtils.watch(conn, keys.toArray(new String[keys.size()]));
				
				for(Iterator<String> iter=request.getProductMap().keySet().iterator();iter.hasNext();) {
					String productCode = iter.next();
					String key = "SecKill_P_"+productCode;
					Long inStore = Long.parseLong(RedisUtils.get(conn, key));
					if(inStore<request.getProductMap().get(productCode)) {//库存不够了
						RedisUtils.unwatch(conn);
						throw new RuntimeException("秒杀失败！");
					}
				}
				Transaction t = RedisUtils.multi(conn);
				
				for(Iterator<String> iter=request.getProductMap().keySet().iterator();iter.hasNext();) {
					String productCode = iter.next();
					String key = "SecKill_P_"+productCode;
					Long inStore = Long.parseLong(RedisUtils.get(conn, key));
					t.incrBy( key, -inStore);
				}
				try {
					RedisUtils.exec(t);
					success = true;
				}catch(RuntimeException e) {
					logger.warn("秒杀失败，可能是并发量大照成的。",e);
					retryTimes--;
					success=false;
				}
				
			}
			
			//调用数据库完成订单业务
			PayOrder order = orederService.place(request.getPayerNo(), request.getCarriageAmt(), request.getProductMap());
			OrderPlaceResponse response = new OrderPlaceResponse();
			PropertyUtils.copyProperties(response, order);
			return response ;
			
		}catch(Exception ex){
			ErrMsg error = new ErrMsg("code",ex.getMessage());
			OrderPlaceResponse response = new OrderPlaceResponse();
			response.setErrMsg(error);
			return response;
		}finally {
			conn.close();
		}
	}
	
	/**
	 * 开始秒杀活动
	 */
	@RequestMapping("/open/{productCode}")
	public void openSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		productService.openSecKill(productCode);
		ResponseUtils.writeToResponse(response, "success!");
	}
	
	/**
	 * 结束秒杀活动。
	 * @param productCodes
	 */
	@RequestMapping("/close/{productCode}")
	public void closeSecKill(HttpServletResponse response,String productCode) {
		productService.closeSecKill(productCode);
		ResponseUtils.writeToResponse(response, "success!");
	}
}
