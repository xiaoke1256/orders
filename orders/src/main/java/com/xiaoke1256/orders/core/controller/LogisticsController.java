package com.xiaoke1256.orders.core.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespCode;
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
	 * 受理一个订单
	 * @return
	 */
	@RequestMapping(value="/acceptOrder/{orderNo}",method= {RequestMethod.POST})
	public RespMsg acceptOrder(@PathVariable("orderNo") String orderNo) {
		try {
			logisticsService.acceptOrder(orderNo);
			return RespMsg.SUCCESS;
		}catch(AppException e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}
	}
	
	/**
	 * 提交一个物流单
	 * @return
	 */
	@RequestMapping(value="/submit",method= {RequestMethod.POST})
	public RespMsg submitLoOrder(@RequestBody LoOrderRequest request) {
		try {
			String subOrderNo = request.getSubOrderNo();
			if(StringUtils.isEmpty(subOrderNo)) {
				throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"SubOrderNo can not be null.");
			}
			String companyCode = request.getCompanyCode();
			if(StringUtils.isEmpty(companyCode)) {
				throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"CompanyCode can not be null.");
			}
			String loOrderNo = request.getLoOrderNo();
			if(StringUtils.isEmpty(loOrderNo)) {
				throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"LoOrderNo can not be null.");
			}
			
			logisticsService.submitLoOrder(subOrderNo, companyCode, loOrderNo);
			
			return RespMsg.SUCCESS;
		}catch(AppException e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}
	}
	
	/**
	 * 确认收货
	 * @param subOrderNo
	 * @return
	 */
	@RequestMapping(value="/confirmReceived",method= {RequestMethod.POST})
	public RespMsg confirmReceived(@RequestParam("subOrderNo") String subOrderNo) {
		try {
			if(StringUtils.isEmpty(subOrderNo)) {
				throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"SubOrderNo can not be null.");
			}
			logisticsService.confirmReceived(subOrderNo);
			return RespMsg.SUCCESS;
		}catch(AppException e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
		}
	}
}
