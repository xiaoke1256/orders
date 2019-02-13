package com.xiaoke1256.orders.product.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.product.service.ProductService;

/**
 * 提供对秒杀活动的支持。
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/secKill")
public class SecKillSupportController {
	@Autowired
	private ProductService productService;
	/**
	 * 开始秒杀活动
	 */
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		if(StringUtils.isBlank(productCode))
			return new ErrMsg("9","error:ProductCode can not be null.");
		productService.openSecKill(productCode);
		return new RespMsg("0","success!");
		
	}
	
	/**
	 * 结束秒杀活动。
	 * @param productCodes
	 */
	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(HttpServletResponse response,@PathVariable("productCode") String productCode) {
		productService.closeSecKill(productCode);
		return new RespMsg("0","success!");
	}
}
