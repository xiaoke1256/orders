package com.xiaoke1256.orders.core.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.thirdpayplatform.dto.AckRequest;

/**
 * 第三方支付的客户
 * @author Administrator
 *
 */
@Component
public class ThirdPaymentClient {
	@Value("${payment.remote.ack.uri}")
	private String ackUri;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public RespMsg acceptNote(String orderNo,String isSuccess) {
		AckRequest request = new AckRequest();
		request.setOrderNo(orderNo);
		request.setIsSuccess(isSuccess);
		RespMsg resp = restTemplate.postForObject(ackUri, request, RespMsg.class);
		return resp;
	}
}
