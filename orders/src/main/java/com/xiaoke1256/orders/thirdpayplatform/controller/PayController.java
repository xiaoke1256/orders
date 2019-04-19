package com.xiaoke1256.orders.thirdpayplatform.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.common.security.MD5Util;
import com.xiaoke1256.orders.common.security.ThreeDESUtil;
import com.xiaoke1256.orders.thirdpayplatform.bo.ThirdPayOrder;
import com.xiaoke1256.orders.thirdpayplatform.dto.PayRequest;
import com.xiaoke1256.orders.thirdpayplatform.dto.PayResp;
import com.xiaoke1256.orders.thirdpayplatform.service.ThirdPayService;

@Controller
public class PayController {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	@Autowired
	private ThirdPayService thirdPayService;
	
	/**与第三方支付平台约定的秘钥*/
	@Value("${third_pay.key}")
	private String key;
	
	/**
	 * 支付
	 * @return
	 */
	@RequestMapping("/pay")
	public RespMsg pay(PayRequest payRequest) {
		try {
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<10) {
				throw new IOException("支付失败。");//模拟10%的失败概率。
			}
			String payerNo = payRequest.getPayerNo();
			String payeeNo = payRequest.getPayeeNo();
			String orderType = payRequest.getOrderType();
			BigDecimal amt = payRequest.getAmt();
			String remark = payRequest.getRemark();
			ThirdPayOrder order = thirdPayService.pay(payerNo, payeeNo, amt, orderType, remark);
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<10) {
				throw new IOException("支付失败。");//模拟10%的失败概率。
			}
			String verifyCode = makeVerifyCode(order.getOrderNo(),remark);
			return new PayResp(ErrorCode.SUCCESS,order.getOrderNo(),verifyCode);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ErrMsg(e);
		}
	}
	
	/**
	 * 接收到通知。
	 * @param isSuccess
	 * @return
	 */
	@RequestMapping("/accept_note")
	public RespMsg acceptNote(String orderNo,String isSuccess) {
		try {
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<10) {
				throw new IOException("支付失败。");//模拟10%的失败概率。
			}
			
			if("SUCCESS".equals(isSuccess)) {
				//最终处理支付成功。
				thirdPayService.success(orderNo);
			}else if("FAIL".equals(isSuccess)) {
				//支付失败
				thirdPayService.fail(orderNo);
			}else {
				new ErrMsg(ErrorCode.WRONG_PARAMTER_ERROR);
			}
			
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<10) {
				throw new IOException("支付失败。");//模拟10%的失败概率。
			}
			return new RespMsg(ErrorCode.SUCCESS);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ErrMsg(e);
		}
	}
	
	/**
	 * 生成校验码
	 * 先Md5算法，然后用实现约定的算法(3DES)加密。
	 * 注意：如果用于生产环境应考虑不对称加密。
	 * @return
	 * @throws Exception 
	 */
	private String makeVerifyCode(String orderNo ,String remark) throws Exception {
		return ThreeDESUtil.encryptThreeDESECB(MD5Util.getMD5(orderNo+"-"+remark), key) ;
	}
}
