package com.xiaoke1256.orders.product.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.xiaoke1256.orders.common.RespMsg;

/**
 * 对秒杀业务提供支持的Service
 * @author xiaoke_1256
 *
 */
public interface SecKillSupportService {
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(@PathVariable("productCode")String productCode);
	
	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(@PathVariable("productCode")String productCode);
}
