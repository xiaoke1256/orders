package com.xiaoke1256.orders.thirdpayplatform.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.ErrorCode;
import com.xiaoke1256.orders.common.security.MD5Util;
import com.xiaoke1256.orders.common.security.ThreeDESUtil;
import com.xiaoke1256.orders.thirdpayplatform.bo.ThirdPayOrder;
import com.xiaoke1256.orders.thirdpayplatform.dto.AckRequest;
import com.xiaoke1256.orders.thirdpayplatform.dto.OrderResp;
import com.xiaoke1256.orders.thirdpayplatform.dto.PayRequest;
import com.xiaoke1256.orders.thirdpayplatform.dto.PayResp;
import com.xiaoke1256.orders.thirdpayplatform.dto.ThirdPayOrderDto;
import com.xiaoke1256.orders.thirdpayplatform.service.ThirdPayService;

@Controller
@RequestMapping("/thirdpay")
public class PayController {
	
	private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
	
	@Autowired
	private ThirdPayService thirdPayService;
	
	/**与第三方支付平台约定的秘钥*/
	@Value("${third_pay_platform.key}")
	private String key;
	
	/**
	 * 支付
	 * @return
	 */
	@RequestMapping(value="/pay",method={RequestMethod.POST})
	public RespMsg pay(PayRequest payRequest) {
		try {
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<5) {
				throw new IOException("支付失败。");//模拟5%的失败概率。
			}
			String payerNo = payRequest.getPayerNo();
			String payeeNo = payRequest.getPayeeNo();
			String orderType = payRequest.getOrderType();
			BigDecimal amt = payRequest.getAmt();
			String remark = payRequest.getRemark();
			String palteform = payRequest.getPalteform();
			ThirdPayOrder order = thirdPayService.pay(payerNo, payeeNo, amt, orderType,palteform, remark);
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<5) {
				throw new IOException("支付失败。");//模拟5%的失败概率。
			}
			String verifyCode = makeVerifyCode(order.getOrderNo(),remark);
			return new PayResp(ErrorCode.SUCCESS,order.getOrderNo(),verifyCode);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ErrMsg(e);
		}
	}
	
	/**
	 * 获取订单的详细信息
	 * @param orderNo
	 * @return
	 */
	@RequestMapping(value="/{orderNo}",method={RequestMethod.GET})
	public RespMsg queryOrder(@PathVariable("orderNo") String orderNo){
		try {
			//TODO 校验调用方的身份，权限。
			//TODO 校验订单是否是是调用方建立的。
			ThirdPayOrder order = thirdPayService.getByOrderNo(orderNo);
			ThirdPayOrderDto orderDto = new ThirdPayOrderDto();
			BeanUtils.copyProperties(orderDto, order);
			return new OrderResp(orderDto);
		}catch(Exception ex) {
			logger.error(ex.getMessage(),ex);
			return new ErrMsg(ex);
		}
	}
	
	/**
	 * 接收到通知。
	 * @param isSuccess
	 * @return
	 */
	@RequestMapping(value="/ack",method={RequestMethod.POST})
	public RespMsg acceptNote(@RequestBody AckRequest request) {
		try {
			String orderNo = request.getOrderNo();
			String isSuccess = request.getIsSuccess();
			//TODO 校验调用方的身份，权限。
			//TODO 校验订单是否是是调用方建立的。
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<2) {
				throw new IOException("通知失败。");//模拟2%的失败概率。
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
			if(RandomUtils.nextInt(100)<2) {
				throw new IOException("通知失败。");//模拟2%的失败概率。
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
		String randomNum = Integer.toHexString(new Random().nextInt());//随机值，用于混淆加密过程。
		return ThreeDESUtil.encryptThreeDESECB(randomNum+MD5Util.getMD5(orderNo+"-"+remark), key) ;
	}
}
