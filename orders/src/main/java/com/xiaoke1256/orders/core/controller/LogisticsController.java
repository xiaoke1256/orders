package com.xiaoke1256.orders.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.dto.LoOrderRequest;
import com.xiaoke1256.orders.core.service.LogisticsService;

/**
 * 物流模块
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsController {
	private static  final Logger logger = LoggerFactory.getLogger(LogisticsController.class);
	
	@Autowired
	private LogisticsService logisticsService;
	
	/**
	 * 提交一个物流单
	 * @return
	 */
	public RespMsg submitLoOrder(LoOrderRequest request) {
		try {
			String subOrderNo = request.getSubOrderNo();
			String companyCode = request.getCompanyCode();
			String loOrderNo = request.getLoOrderNo();
			
			logisticsService.submitLoOrder(subOrderNo, companyCode, loOrderNo);
			
			return RespMsg.SUCCESS;
		}catch(AppException e) {
			logger.error(e.getMessage(), e);
			return new ErrMsg(e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new ErrMsg(e);
		}
	}
	
}
