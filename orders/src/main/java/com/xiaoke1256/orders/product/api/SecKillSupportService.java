package com.xiaoke1256.orders.product.api;

import com.xiaoke1256.orders.common.RespMsg;

/**
 * 秒杀活动接口
 * @author Administrator
 *
 */
public interface SecKillSupportService {
	public RespMsg openSecKill(String productCode);
	
	public RespMsg closeSecKill(String productCode);
}
