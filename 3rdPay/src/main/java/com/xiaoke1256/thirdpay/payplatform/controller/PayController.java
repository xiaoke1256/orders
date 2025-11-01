package com.xiaoke1256.thirdpay.payplatform.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.xiaoke1256.thirdpay.payplatform.dto.*;
import com.xiaoke1256.thirdpay.payplatform.service.MerchantService;
import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import com.xiaoke1256.thirdpay.sdk.encryption.aes.AESUtils;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAUtils;
import com.xiaoke1256.thirdpay.sdk.vo.OrderFormInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.security.MD5Util;
import com.xiaoke1256.orders.common.security.ThreeDESUtil;
import com.xiaoke1256.thirdpay.payplatform.bo.ThirdPayOrder;
import com.xiaoke1256.thirdpay.payplatform.service.ThirdPayService;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	private static final Logger logger = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private ThirdPayService thirdPayService;
	
	@Autowired
	private MerchantService merchantService;
	
	/**与第三方支付平台约定的秘钥*/
	@Value("${third_pay_platform.key}")
	private String key;

	/**
	 * 预支付 (校验支付信息，校验支付信息)
	 * @param prePayRequest
	 * @return
	 */

	@RequestMapping(value="/prePay",method={RequestMethod.POST})
	public RespMsg prePay(@RequestBody PrePayRequest prePayRequest) throws Exception {
		//从orderStr 中解析出订单信息。
		String payFormStr = prePayRequest.getPayFormStr();
		String orderFormJson = new String(Base64.getDecoder().decode(payFormStr), StandardCharsets.UTF_8);
		OrderFormInfo orderFormInfo = JSON.parseObject(orderFormJson, OrderFormInfo.class);
		Long expiredTime = orderFormInfo.getExpiredTime();
		if(expiredTime<System.currentTimeMillis()) {
			return new PayResp(RespCode.BUSSNESS_ERROR,"支付超时。");
		}
		//平台方私钥
		PrivateKey privateKey = RSAKeyPairGenerator.loadPrivateKeyFromStream(getClass().getClassLoader().getResourceAsStream("/3rdPay/cryptionkeys/private_key.pem"));
		String key = RSAUtils.decrypt(orderFormInfo.getKey() , privateKey) ;
		String orderInfoJson = AESUtils.decrypt(orderFormInfo.getOrderInfo(), AESUtils.loadAESKey(key));
		OrderInfo orderInfo = JSON.parseObject(orderInfoJson, OrderInfo.class);
		if(!orderInfo.getExpiredTime().equals(orderFormInfo.getExpiredTime())) {
			return new PayResp(RespCode.SECURE_ERROR,"过期时间被人篡改过。");
		}
		//验证签名（用商户的公钥验签）。
		//先获取商户的公钥
		String publicKey = merchantService.getPublicKey(orderInfo.getMerchantNo());
		if(!RSAUtils.verifySignature(orderInfoJson.getBytes(StandardCharsets.UTF_8), Base64.getDecoder().decode(orderFormInfo.getSign()), RSAKeyPairGenerator.loadPublicKey(publicKey))) {
			return new PayResp(RespCode.BUSSNESS_ERROR,"签名验证失败。");
		}

		//填充收款方支付账号。
		String merchantAccNo = merchantService.getAccNo(orderInfo.getMerchantNo());
		orderInfo.setMerchantPayeeNo(merchantAccNo);
		//填充付款方支付账号。（正式环境应该从session中获取，现在随机获取）
		//从账号表中选取账户名一样的账号。找不到就随机选一个。
		orderInfo.setMerchantPayerNo(merchantAccNo);
		// 吊起支付页面（让用户输入支付密码）
		return new RespMsg(RespCode.SUCCESS, "成功吊起订单信息",orderInfo);
	}

	/**
	 * 支付
	 * @return
	 */
	@RequestMapping(value="/pay",method={RequestMethod.POST})
	public PayResp pay(@RequestBody PayRequest payRequest) {
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
			//TODO 检查付款码是否正确。
			ThirdPayOrder order = thirdPayService.pay(payerNo, payeeNo, amt, orderType,palteform, remark);
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<5) {
				throw new IOException("支付失败。");//模拟5%的失败概率。
			}
			String verifyCode = makeVerifyCode(order.getOrderNo(),remark);
			return new PayResp(RespCode.SUCCESS,verifyCode,order.getOrderNo());
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new PayResp(e);
		}
	}
	
	/**
	 * 获取订单的详细信息
	 * @param orderNo
	 * @return
	 */
	@GetMapping("/orders/{orderNo}")
	public OrderResp queryOrder(@PathVariable("orderNo") String orderNo){
		try {
			//TODO 校验调用方的身份，权限。
			//TODO 校验订单是否是是调用方建立的。
			ThirdPayOrder order = thirdPayService.getByOrderNo(orderNo);
			ThirdPayOrderDto orderDto = new ThirdPayOrderDto();
			BeanUtils.copyProperties(orderDto, order);
			return new OrderResp(orderDto);
		}catch(Exception ex) {
			logger.error(ex.getMessage(),ex);
			return new OrderResp(ex);
		}
	}
	
	/**
	 * 接收到通知。
	 * @param request
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
				new RespMsg(RespCode.WRONG_PARAMTER_ERROR);
			}
			
			Thread.sleep(50+RandomUtils.nextInt(50));//模拟网络不稳定
			if(RandomUtils.nextInt(100)<2) {
				throw new IOException("通知失败。");//模拟2%的失败概率。
			}
			return new RespMsg(RespCode.SUCCESS);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new RespMsg(e);
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
