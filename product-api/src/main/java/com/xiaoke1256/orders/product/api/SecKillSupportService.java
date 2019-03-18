package com.xiaoke1256.orders.product.api;

import com.xiaoke1256.orders.common.RespMsg;

/**
 * 对秒杀业务提供支持的Service
 * @author xiaoke_1256
 *
 */
public interface SecKillSupportService {
	public RespMsg openSecKill(String productCode);
	
	public RespMsg closeSecKill(String productCode);
}
