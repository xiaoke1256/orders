package com.xiaoke1256.orders.product.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.common.RespMsg;

/**
 * 对秒杀业务提供支持的Service
 * @author xiaoke_1256
 *
 */
public interface SecKillSupportService {
	@RequestMapping(value="/secKill/open/{productCode}",method=RequestMethod.POST)
	public RespMsg openSecKill(@PathVariable("productCode")String productCode);
	
	@RequestMapping(value="/secKill/close/{productCode}",method=RequestMethod.POST)
	public RespMsg closeSecKill(@PathVariable("productCode")String productCode);
}
