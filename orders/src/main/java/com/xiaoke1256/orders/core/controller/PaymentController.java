package com.xiaoke1256.orders.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.core.dto.PaymentNoticeRequest;
import com.xiaoke1256.orders.core.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	private PaymentService paymentService;
	/**
	 * 处理第三方支付后的反馈
	 */
	@RequestMapping("/notice")
	public RespMsg payNotice(PaymentNoticeRequest request) {
		String orderNo = request.getOrderNo();
		String payOrderNo = request.getPayOrderNo();
		String payType = request.getPayType();
		String verifyInfo = request.getVerifyCode();
		//TODO 解密校验信息。自己校验一下各项信息是否正确
		//TODO 调用第三方支付接口校验支付情况
		
		paymentService.pay(payOrderNo, orderNo, payType);
		
		//成功则通知第三方支付平台
		
		//失败也要通知第三方支付平台
		
		return RespMsg.SUCCESS;
	}
	
	/**
	 * 第三方支付通知我们，取消支付
	 * @param request
	 * @return
	 */
	@RequestMapping("/cancel")
	public RespMsg cancel(PaymentCancelRequest request) {
		return RespMsg.SUCCESS;
	}
}
