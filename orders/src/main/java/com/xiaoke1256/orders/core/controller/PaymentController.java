package com.xiaoke1256.orders.core.controller;

import java.rmi.RemoteException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.exception.BusinessException;
import com.xiaoke1256.orders.common.security.MD5Util;
import com.xiaoke1256.orders.common.security.ThreeDESUtil;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.orders.core.dto.PayConfig;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.core.dto.PaymentNoticeRequest;
import com.xiaoke1256.orders.core.service.OrederService;
import com.xiaoke1256.orders.core.service.PaymentService;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	
	@Autowired
	private OrederService orederService;
	
	/**与第三方支付平台约定的秘钥*/
	@Value("${third_pay_platform.key}")
	private String key;
	
	/**平台方的支付账号*/
	@Value("${platform.payment.thirdpay.account}")
	private String pateformPayAccount ;
	/**第三方支付平台的地址*/
	@Value("${payment.3rdpay.site.url}")
	private String thirdpaySiteUrl;
	
	@RequestMapping(value="/config",method= {RequestMethod.GET})
	private PayConfig getPayConfig() {
		return new PayConfig(pateformPayAccount,thirdpaySiteUrl);
	}
	
	/**
	 * 处理第三方支付后的反馈
	 */
	@RequestMapping(value="/notice",method= {RequestMethod.POST})
	public RespMsg payNotice(@RequestBody PaymentNoticeRequest request) {
		String orderNo = request.getOrderNo();
		String payOrderNo = request.getPayOrderNo();
		String payType = request.getPayType();
		String verifyInfo = request.getVerifyCode();
		try {
			//解密校验信息。自己校验一下各项信息是否正确
			if(!verify(orderNo,payOrderNo,verifyInfo)) {
				//TODO 疑似黑客攻击，记录日志。
				return new RespMsg(RespCode.BUSSNESS_ERROR.getCode(),"订单校验失败，未完成支付。");
			}
			//检查交易记录,
			ThirdPayOrderDto order = thirdPaymentClient.getOrder(orderNo);
			verifyOrder(order,payOrderNo);
			
			paymentService.notice(orderNo, payType,payOrderNo);
			
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
			logger.error(ex.getMessage(),ex);
			//远程调用异常要将支付取消
			try {
				paymentService.cancel(orderNo, PaymentCancelRequest.CANCEL_TYPE_REMOTE_INVOK,payOrderNo);
			} catch(Exception e) {
				logger.error(e.getMessage(),e);
			}
			return new RespMsg(ex);
		}catch(AppException ex){
			//失败也要通知第三方支付平台
			logger.error(ex.getMessage(),ex);
			thirdPaymentClient.acceptNote(orderNo, "FAIL");
			return new RespMsg(ex);
		}catch(Exception ex){
			//失败也要通知第三方支付平台
			logger.error(ex.getMessage(),ex);
			thirdPaymentClient.acceptNote(orderNo, "FAIL");
			return new RespMsg(ex);
		}
		
	}
	
	/**
	 * 校验
	 * @return
	 */
	private boolean verify(String orderNo,String payOrderNo,String verifyInfo) {
		try {
			String md5 = MD5Util.getMD5(orderNo+"-"+payOrderNo);
			String decode = ThreeDESUtil.decryptThreeDESECB(verifyInfo, key);
			return decode.endsWith(md5);
		}catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 校验订单
	 * @param order
	 * @param payOrderNo
	 */
	private void verifyOrder(ThirdPayOrderDto order,String payOrderNo) {
		//收款人必须是平台方
		//平台方账号，
		if(!pateformPayAccount.equals(order.getPayeeNo())) {
			//TODO 疑似黑客攻击，记录日志。
			throw new BusinessException("订单校验失败，未完成支付。");
		}
		//订单类型必须是消费
		if(!ThirdPayOrderDto.ORDER_TYPE_CONSUME.equals(order.getOrderType())) {
			//TODO 疑似黑客攻击，记录日志。
			throw new BusinessException("订单校验失败，未完成支付。");
		}
		//金额必须与预期一致
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		if(!order.getAmt().equals(payOrder.getTotalAmt())) {
			//TODO 疑似黑客攻击，记录日志。
			throw new BusinessException("订单校验失败，未完成支付。");
		}
	}
}
