package com.xiaoke1256.orders.core.client;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.thirdpay.payplatform.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 第三方支付的客户端
 * @author Administrator
 *
 */
@Component
public class ThirdPaymentClient {
	private static final Logger LOG = LoggerFactory.getLogger(ThirdPaymentClient.class);

	@Value("${payment.remote.ack.uri}")
	private String ackUri;
	
	@Value("${payment.remote.get_order.uri}")
	private volatile String getUri;
	
	/**第三方支付平台的地址*/
	@Value("${payment.3rdpay.site.url}")
	private String thirdpaySiteUrl;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public RespMsg acceptNote(String orderNo,String isSuccess) {
		AckRequest request = new AckRequest();
		request.setOrderNo(orderNo);
		request.setIsSuccess(isSuccess);
		RespMsg resp = restTemplate.postForObject(ackUri, request, RespMsg.class);
		return resp;
	}
	
	public ThirdPayOrderDto getOrder(String orderNo) {
		if(!getUri.endsWith("/"))
			getUri+="/";
		OrderResp resp = restTemplate.getForObject(getUri+orderNo,OrderResp.class);
		if(RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
			return resp.getOrder();
		}else {
			LOG.error("getOrder failed. orderNo:"+orderNo);
			throw new AppException(resp.getCode(),resp.getMsg());
		}
	}
	
	public PayResp pay( PayRequest payRequest) {
		PayResp resp = restTemplate.postForObject(thirdpaySiteUrl, payRequest, PayResp.class);
		return resp;
	}
}
