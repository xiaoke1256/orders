package com.xiaoke1256.orders.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.dto.LoOrderRequest;

/**
 * 物流模块
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsController {
	
	/**
	 * 提交一个物流单
	 * @return
	 */
	public RespMsg submitLoOrder(LoOrderRequest request) {
		return RespMsg.SUCCESS;
	}
	
}
