package com.xiaoke1256.orders.product.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.product.api.SecKillSupportService;
import com.xiaoke1256.orders.product.service.ProductService;

/**
 * 提供对秒杀活动的支持。
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/secKill")
public class SecKillSupportController implements SecKillSupportService {
	@Autowired
	private ProductService productService;
	/**
	 * 开始秒杀活动
	 */
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(@PathVariable("productCode") String productCode) {
		if(StringUtils.isBlank(productCode))
			return new ErrMsg(ErrorCode.EMPTY_PARAMTER_ERROR.getCode(),"error:ProductCode can not be null.");
		productService.openSecKill(productCode);
		return RespMsg.SUCCESS;
		
	}
	
	/**
	 * 结束秒杀活动。
	 * @param productCodes
	 */
	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(@PathVariable("productCode") String productCode) {
		productService.closeSecKill(productCode);
		return RespMsg.SUCCESS;
	}
}
