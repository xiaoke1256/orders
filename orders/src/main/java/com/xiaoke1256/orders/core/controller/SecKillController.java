package com.xiaoke1256.orders.core.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.common.utils.RedisUtils;
import com.xiaoke1256.common.utils.ResponseUtils;
import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.dto.ProductWithStorage;
import com.xiaoke1256.orders.core.dto.ProductWithStorageQueryResult;
import com.xiaoke1256.orders.core.service.OStorageService;
import com.xiaoke1256.orders.core.service.OrederService;
import com.xiaoke1256.orders.core.service.ProductService;
import com.xiaoke1256.orders.product.dto.ProductQueryResult;
import com.xiaoke1256.orders.product.dto.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 实现秒杀业务
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/secKill")
public class SecKillController {
	private static  final Logger logger = LogManager.getLogger(SecKillController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrederService orederService;
	
	@Autowired
	private OStorageService oStorageService;
	
	@Autowired
	private RestTemplate restTemplate;
	
//	@RequestMapping(value="/",method={RequestMethod.GET})
//	public ModelAndView toIndex() {
//		List<Product> products = productService.queryProductsWithLimit(10);
//		ModelAndView view = new ModelAndView();
//		view.setViewName("secKill/index");
//		view.addObject("products", products);
//		return view;
//	}
//	
	
	/**
	 * 查询商品
	 * @return
	 */
	@RequestMapping(value="/products",method={RequestMethod.GET})
	public ProductWithStorageQueryResult queryProduct(ProductCondition condition) {
		int pageNo = condition.getPageNo();
		int pageSize = condition.getPageSize();
		StringBuilder paramsSb = new StringBuilder();
		paramsSb.append("pageNo=").append(pageNo).append("&");
		paramsSb.append("pageSize=").append(pageSize);
		if(StringUtils.isNotBlank(condition.getProductCode())) {
			paramsSb.append("&").append("productCode=").append(condition.getProductCode());
		}
		if(StringUtils.isNotBlank(condition.getProductName())) {
			paramsSb.append("&").append("productName=").append(condition.getProductName());
		}
		ProductQueryResult productResut = restTemplate.getForObject("http://127.0.0.1:8081/product/product/search?"+paramsSb.toString(), ProductQueryResult.class);
		ProductWithStorageQueryResult result = new ProductWithStorageQueryResult(productResut.getPageNo(),productResut.getPageSize(),productResut.getTotalCount());
		List<ProductWithStorage> resultList = productResut.getResultList().stream().map((p)->makeProductWithStorage(p)).collect(Collectors.toList());
		result.setResultList(resultList);
		return result;
	}
	
	private ProductWithStorage makeProductWithStorage(Product p) {
		String productCode = p.getProductCode();
		OStorage storage = oStorageService.getByProductCode(productCode);
		storage.getStockNum();
		ProductWithStorage productWithStorage = new ProductWithStorage();
		BeanUtils.copyProperties(p, productWithStorage);
		productWithStorage.setStockNum(storage.getStockNum());
		productWithStorage.setStoreName(p.getStore().getStoreName());
		logger.info("p.getInSeckill()="+p.getInSeckill());
		//productWithStorage.setProductTypeNames(productTypeNames);TODO 暂不处理
		return productWithStorage;
	}
	/**
	 * 下订单（利用redis缓存）
	 */
	@RequestMapping(value="/place",method={RequestMethod.POST})
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
					OStorage p = oStorageService.getByProductCode(productCode);
					if(productService.isInSecKill(productCode)) {
						RedisUtils.set(conn, key, String.valueOf(p.getStockNum()));
					}else {
						throw new RuntimeException("This product is not in seckill!");
					}
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
						throw new RuntimeException("秒杀失败！（被抢完了）");
					}
				}
				
				Transaction t = RedisUtils.multi(conn);
				for(Iterator<String> iter=request.getProductMap().keySet().iterator();iter.hasNext();) {
					String productCode = iter.next();
					String key = "SecKill_P_"+productCode;
					t.incrBy( key, -request.getProductMap().get(productCode));
					
				}
				
				try {
					RedisUtils.exec(t);
					success = true;
				}catch(RuntimeException e) {
					logger.warn("秒杀失败，可能是并发量大造成的。",e);
					retryTimes--;
					success=false;
				}
				
			}
			
			if(!success)
				throw new RuntimeException("秒杀失败！");

			//调用数据库完成订单业务
			PayOrder order = orederService.place(request.getPayerNo(), request.getProductMap());
			OrderPlaceResponse response = new OrderPlaceResponse();
			PropertyUtils.copyProperties(response, order);
			return response ;
			
		}catch(Exception ex){
			logger.error(ex, ex);
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
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		RespMsg respMsg = restTemplate.postForObject("http://127.0.0.1:8081/product/secKill/open/"+productCode,null, RespMsg.class);
		if(!"0".equals(respMsg.getCode())) {
			logger.error(respMsg.getCode()+":"+respMsg.getMsg());
			return respMsg;
		}
		OStorage storage = oStorageService.getByProductCode(productCode);
		
		if(storage.getStockNum()<100) {
			throw new RuntimeException(""+productCode+"的库存不足,库存需大于100份才可支持秒杀活动。");
		}
		
		Jedis conn = RedisUtils.connect();
		RedisUtils.set(conn, "SecKill_P_"+storage.getProductCode(), String.valueOf(storage.getStockNum()));
		conn.close();
		
		return respMsg;
	}
	
	/**
	 * 结束秒杀活动。
	 * @param productCodes
	 */
	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		RespMsg respMsg =  restTemplate.postForObject("http://127.0.0.1:8081/product/secKill/close/"+productCode,null, RespMsg.class);
		if(!"0".equals(respMsg.getCode())) {
			logger.error(respMsg.getCode()+":"+respMsg.getMsg());
			return respMsg;
		}
		Jedis conn = RedisUtils.connect();
		try {
			RedisUtils.del(conn, "SecKill_P_"+productCode);
		}catch(RuntimeException e) {
			logger.warn(e);
		}
		conn.close();
		return respMsg;
	}
}
