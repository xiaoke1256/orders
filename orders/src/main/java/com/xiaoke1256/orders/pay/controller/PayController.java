package com.xiaoke1256.orders.pay.controller;

import com.xiaoke1256.orders.core.service.PaymentService;
import com.xiaoke1256.thirdpay.sdk.PayClient;
import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import org.apache.commons.beanutils.PropertyUtils;
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
import com.xiaoke1256.orders.pay.service.PayBusinessConfig;
import com.xiaoke1256.orders.pay.service.PayBusinessService;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/pay")
public class PayController {
	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	@Autowired
	private PayBusinessConfig payBusinessConfig;
	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value="/getPayFormStr",method= {RequestMethod.POST})
	public RespMsg getPayFormStr(@RequestBody ThirdPayOrderDto order) throws Exception {
		//保存订单信息
		paymentService.savePayment(order);
		OrderInfo orderInfo = new OrderInfo();
		PropertyUtils.copyProperties(orderInfo, order);
		try( InputStream is = PayClient.class.getResourceAsStream("/keys/private_key.pem")){
			String payFormStr = PayClient.generateOrderFormString(orderInfo, RSAKeyPairGenerator.loadPrivateKeyFromStream(is));
			 return new RespMsg(RespCode.SUCCESS,payFormStr);
		}
	}
	
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
		
		//根据OrderType获取不同的支付业务对象。
		String orderType = order.getOrderType();
		PayBusinessService businessServices = payBusinessConfig.getPayBusinessService(orderType);
		businessServices.cancel(order.getMerchantOrderNo(), request.getCancelType(), order.getRemark());
		
		return RespMsg.SUCCESS;
	}
}
