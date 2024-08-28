package com.xiaoke1256.orders.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.QueryResultResp;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.core.client.ProductQueryClient;
import com.xiaoke1256.orders.core.dto.ProductWithStorage;
import com.xiaoke1256.orders.core.dto.ProductWithStorageQueryResult;
import com.xiaoke1256.orders.core.service.OStorageService;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;

/**
 * 本类专门用于查询商品
 * @author Administrator
 *
 */
@RestController
public class ProductQueryController {
	private static  final Logger logger = LoggerFactory.getLogger(ProductQueryController.class);
	
	@Autowired
	private ProductQueryClient productQueryService;
	
	@Autowired
	private OStorageService oStorageService;
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
			//Map<String,Object> paramsMap = new HashMap<>();
//			paramsMap.put("pageNo", pageNo);
//			paramsMap.put("pageSize", pageSize);
//			if(StringUtils.isNotBlank(condition.getProductCode())) {
//				paramsMap.put("productCode", condition.getProductCode());
//			}
//			if(StringUtils.isNotBlank(condition.getProductName())) {
//				paramsMap.put("productName", condition.getProductName());
//			}
			RespMsg respMsg = productQueryService.searchProductByCondition(condition);
			if(!RespMsg.SUCCESS.getCode().equals(respMsg.getCode())) {
				return respMsg;
			}
			SimpleProductQueryResultResp productResut = (SimpleProductQueryResultResp)respMsg;
			ProductWithStorageQueryResult result = new ProductWithStorageQueryResult(productResut.getPageNo(),productResut.getPageSize(),productResut.getTotalCount());
			List<ProductWithStorage> resultList = productResut.getResultList().stream().map((p)->makeProductWithStorage((SimpleProduct)p)).collect(Collectors.toList());
			result.setResultList(resultList);
			return new QueryResultResp<ProductWithStorage>(result);
		}catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			return new RespMsg("99",ex.getMessage());
		}
	}
	
	private ProductWithStorage makeProductWithStorage(SimpleProduct p) {
		String productCode = p.getProductCode();
		OStorage storage = oStorageService.getByProductCode(productCode);
		ProductWithStorage productWithStorage = new ProductWithStorage();
		BeanUtils.copyProperties(p, productWithStorage);
		productWithStorage.setStockNum(storage==null?0:storage.getStockNum());
		productWithStorage.setStoreName(p.getStoreName());
		logger.info("p.getInSeckill()="+p.getInSeckill());
		//productWithStorage.setProductTypeNames(productTypeNames);TODO 暂不处理
		return productWithStorage;
	}
}
