package com.xiaoke1256.orders.pay.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.core.service.PaymentService;
import com.xiaoke1256.orders.pay.service.PayBusinessConfig;
import com.xiaoke1256.orders.pay.service.PayBusinessService;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;
import com.xiaoke1256.thirdpay.sdk.PayClient;
import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/pay")
public class PayController {

	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PayController.class);

	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	@Autowired
	private PayBusinessConfig payBusinessConfig;
	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value="/getPayFormStr",method= {RequestMethod.POST})
	public RespMsg getPayFormStr(@RequestBody ThirdPayOrderDto order) throws Exception {
		try {
			//保存订单信息
			PaymentTxn paymentTxn = paymentService.savePayment(order);
			OrderInfo orderInfo = new OrderInfo();
			PropertyUtils.copyProperties(orderInfo, order);
			orderInfo.setPayeeNo("000000000000000000");//消费支付的订单收款方默认都是orders平台
			orderInfo.setMerchantPayeeNo("orders");
			orderInfo.setBussinessNo(paymentTxn.getPaymentId()+"");
			try( InputStream is = PayClient.class.getResourceAsStream("/keys/private_key.pem")){
				String payFormStr = PayClient.generateOrderFormString(orderInfo, RSAKeyPairGenerator.loadPrivateKeyFromStream(is));
				return new RespMsg(RespCode.SUCCESS,"success",payFormStr);
			}
		} catch (AppException e) {
			LOGGER.error(e.getMessage(), e);
			return new RespMsg(e);
		}

	}

	/**
	 * 支付平台处理成功，回调我们
	 * @return
	 */
	@RequestMapping(value="/callback_success",method= {RequestMethod.GET})
	public ModelAndView callbackSuccess(@RequestParam("orderNo") String thirdPayOrderNo,
										@RequestParam("merchantOrderNo") String merchantOrderNo,
										@RequestParam("bussinessNo") String bussinessNo) throws ServletException, IOException {
		//改掉订单状态
		paymentService.payed(thirdPayOrderNo,Long.parseLong(bussinessNo));
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("order_id", "TestOrderId");
		modelAndView.setViewName("/payment/callback_success.html"); // 注意路径的正确性
		return modelAndView;
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
