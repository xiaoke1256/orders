package com.xiaoke1256.orders.core.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.product.api.SecKillSupportService;

@FeignClient(name="api-product",url="${remote.api.product.uri}")
@RequestMapping("/secKill")
public interface SecKillSupportClient extends SecKillSupportService {

	/**
	 * 开始秒杀活动
	 */
	@PostMapping("/open/{productCode}")
	public RespMsg openSecKill(@PathVariable("productCode")String productCode);

	@PostMapping("/close/{productCode}")
	public RespMsg closeSecKill(@PathVariable("productCode")String productCode);

}
