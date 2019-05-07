package com.xiaoke1256.orders.pay.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;

@RestController
@RequestMapping("/pay")
public class PayController {
	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	
	/**
	 * 第三方支付平台通知我们，取消支付
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/cancel",method= {RequestMethod.POST})
	public RespMsg cancel(@RequestBody PaymentCancelRequest request) {
		if(StringUtils.isEmpty(request.getCancelType())) {
			throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"未提供足够的参数");
		}
		
		if(StringUtils.isEmpty(request.getOrederNo())){
			throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"未提供足够的参数");
		}
		ThirdPayOrderDto order = thirdPaymentClient.getOrder(request.getOrederNo());
		
		//根据OrderBy获取不同的支付业务对象。
		String orderType = order.getOrderType();
		
		return RespMsg.SUCCESS;
	}
}
