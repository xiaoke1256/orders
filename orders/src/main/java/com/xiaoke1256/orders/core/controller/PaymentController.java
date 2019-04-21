package com.xiaoke1256.orders.core.controller;

import java.rmi.RemoteException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.core.dto.PaymentNoticeRequest;
import com.xiaoke1256.orders.core.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	/**
	 * 处理第三方支付后的反馈
	 */
	@RequestMapping("/notice")
	public RespMsg payNotice(PaymentNoticeRequest request) {
		String orderNo = request.getOrderNo();
		String payOrderNo = request.getPayOrderNo();
		String payType = request.getPayType();
		String verifyInfo = request.getVerifyCode();
		try {
			//TODO 解密校验信息。自己校验一下各项信息是否正确
			//TODO 调用第三方支付接口校验支付情况
			
			paymentService.pay(payOrderNo, orderNo, payType);
			
			//成功则通知第三方支付平台
			try {
				RespMsg resp = thirdPaymentClient.acceptNote(orderNo, "SUCCESS");
				if(!RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
					throw new RemoteException(resp.getCode()+":"+resp.getMsg());
				}
			}catch(RestClientException ex ) {
				throw new RemoteException(ex.getMessage(),ex);
			}			
			return RespMsg.SUCCESS;
		}catch(RemoteException ex) {
			//远程调用异常要将支付取消
			PaymentCancelRequest canelRequest = new PaymentCancelRequest(orderNo, payOrderNo, PaymentCancelRequest.CANCEL_TYPE_REMOTE_INVOK);
			this.cancel(canelRequest );
			return new ErrMsg(ex);
		}catch(Exception ex){
			//失败也要通知第三方支付平台
			thirdPaymentClient.acceptNote(orderNo, "FAIL");
			return new ErrMsg(ex);
		}
		
		
		
		
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
