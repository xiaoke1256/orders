package com.xiaoke1256.orders.core.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.thirdpayplatform.dto.AckRequest;
import com.xiaoke1256.orders.thirdpayplatform.dto.OrderResp;
import com.xiaoke1256.orders.thirdpayplatform.dto.ThirdPayOrderDto;

/**
 * 第三方支付的客户
 * @author Administrator
 *
 */
@Component
public class ThirdPaymentClient {
	@Value("${payment.remote.ack.uri}")
	private String ackUri;
	
	@Value("${payment.remote.get_order.uri}")
	private String getUri;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public RespMsg acceptNote(String orderNo,String isSuccess) {
		AckRequest request = new AckRequest();
		request.setOrderNo(orderNo);
		request.setIsSuccess(isSuccess);
		RespMsg resp = restTemplate.postForObject(ackUri, request, RespMsg.class);
		return resp;
	}
	
	public ThirdPayOrderDto getOrder(String orderNo) {
		RespMsg resp = restTemplate.postForObject(getUri+orderNo, null, RespMsg.class);
		if(RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
			return ((OrderResp)resp).getOrder();
		}else {
			throw new AppException(resp.getCode(),resp.getMsg());
		}
	}
}
