package com.xiaoke1256.orders.core.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.common.utils.RedisUtils;
import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.client.SecKillSupportClient;
import com.xiaoke1256.orders.core.dto.ProductWithStorage;
import com.xiaoke1256.orders.core.dto.ProductWithStorageQueryResult;
import com.xiaoke1256.orders.core.service.OStorageService;
import com.xiaoke1256.orders.core.service.OrederService;
import com.xiaoke1256.orders.core.service.ProductService;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResult;
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
	private static  final Logger logger = LoggerFactory.getLogger(SecKillController.class);
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrederService orederService;
	
	@Autowired
	private OStorageService oStorageService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SecKillSupportClient secKillSupportClient;
	
	@Value("${remote.api.product.uri}")
	private String productApiUri;
	
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
	//@HystrixCommand(fallbackMethod="connectFail")
	@RequestMapping(value="/products",method={RequestMethod.GET})
	public RespMsg queryProduct(ProductCondition condition) {
		try {
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
			SimpleProductQueryResult productResut = restTemplate.getForObject(productApiUri+"/product/search?"+paramsSb.toString(), SimpleProductQueryResult.class);
			ProductWithStorageQueryResult result = new ProductWithStorageQueryResult(productResut.getPageNo(),productResut.getPageSize(),productResut.getTotalCount());
			List<ProductWithStorage> resultList = productResut.getResultList().stream().map((p)->makeProductWithStorage(p)).collect(Collectors.toList());
			result.setResultList(resultList);
			return new QueryResultResp("0","success!",result);
		}catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			return new ErrMsg("99",ex.getMessage());
		}
	}
	
	private ProductWithStorage makeProductWithStorage(SimpleProduct p) {
		String productCode = p.getProductCode();
		OStorage storage = oStorageService.getByProductCode(productCode);
		storage.getStockNum();
		ProductWithStorage productWithStorage = new ProductWithStorage();
		BeanUtils.copyProperties(p, productWithStorage);
		productWithStorage.setStockNum(storage.getStockNum());
		productWithStorage.setStoreName(p.getStoreName());
		logger.info("p.getInSeckill()="+p.getInSeckill());
		//productWithStorage.setProductTypeNames(productTypeNames);TODO 暂不处理
		return productWithStorage;
	}
	/**
	 * 下订单（利用redis缓存）
	 */
	//@HystrixCommand(fallbackMethod="connectFail")
	@RequestMapping(value="/place",method={RequestMethod.POST})
	public RespMsg placeOrder(@RequestBody OrderPlaceRequest request) {
		if(request.getProductMap().isEmpty()) {
			return new ErrMsg(ErrorCode.BUSSNESS_ERROR.getCode(),"空订单！");
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
						throw new BusinessException("This product is not in seckill!");
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
						throw new BusinessException("秒杀失败！（被抢完了）");
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
		
		}catch(BusinessException ex){
			logger.error(ex.getMessage(), ex);
			ErrMsg error = new ErrMsg(ex);
			return error;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			ErrMsg error = new ErrMsg(ex);
			return error;
		}finally {
			conn.close();
		}
	}
	
	/**
	 * 开始秒杀活动
	 */
	//@HystrixCommand(fallbackMethod="connectFail")
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		try {
			RespMsg respMsg = secKillSupportClient.openSecKill(productCode);
			if(!"0".equals(respMsg.getCode())) {
				logger.error(respMsg.getCode()+":"+respMsg.getMsg());
				return respMsg;
			}
			OStorage storage = oStorageService.getByProductCode(productCode);
			
			if(storage.getStockNum()<100) {
				throw new BusinessException(""+productCode+"的库存不足,库存需大于100份才可支持秒杀活动。");
			}
			
			Jedis conn = RedisUtils.connect();
			RedisUtils.set(conn, "SecKill_P_"+storage.getProductCode(), String.valueOf(storage.getStockNum()));
			conn.close();
			
			return respMsg;
		}catch(BusinessException ex){
			logger.error(ex.getMessage(), ex);
			ErrMsg error = new ErrMsg(ex);
			return error;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			ErrMsg error = new ErrMsg(ex);
			return error;
		}
	}
	
	/**
	 * 结束秒杀活动。
	 * @param productCodes
	 */
	//@HystrixCommand(fallbackMethod="connectFail")
	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		RespMsg respMsg = secKillSupportClient.closeSecKill(productCode);
		if(!"0".equals(respMsg.getCode())) {
			logger.error(respMsg.getCode()+":"+respMsg.getMsg());
			return respMsg;
		}
		Jedis conn = RedisUtils.connect();
		try {
			RedisUtils.del(conn, "SecKill_P_"+productCode);
		}catch(RuntimeException e) {
			logger.warn(e.getMessage(),e);
		}
		conn.close();
		return respMsg;
	}
	
}
