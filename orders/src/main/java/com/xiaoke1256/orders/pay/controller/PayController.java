package com.xiaoke1256.orders.pay.controller;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.PaymentTxn;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.orders.core.dto.GetPayFormStrRequest;
import com.xiaoke1256.orders.core.dto.PaymentCancelRequest;
import com.xiaoke1256.orders.core.service.OrederService;
import com.xiaoke1256.orders.core.service.PaymentService;
import com.xiaoke1256.orders.pay.service.PayBusinessConfig;
import com.xiaoke1256.orders.pay.service.PayBusinessService;
import com.xiaoke1256.thirdpay.payplatform.dto.ThirdPayOrderDto;
import com.xiaoke1256.thirdpay.sdk.PayClient;
import com.xiaoke1256.thirdpay.sdk.dto.NotifyInfo;
import com.xiaoke1256.thirdpay.sdk.dto.OrderInfo;
import com.xiaoke1256.thirdpay.sdk.encryption.rsa.RSAKeyPairGenerator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

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
	@Autowired
	private OrederService orederService;

	@RequestMapping(value="/getPayFormStr",method= {RequestMethod.POST})
	public RespMsg getPayFormStr(@RequestBody GetPayFormStrRequest orderRequest) throws Exception {
		try {
			String orderNo = orderRequest.getMerchantOrderNo();
			if(StringUtils.isEmpty(orderNo)) {
				throw new AppException(RespCode.EMPTY_PARAMTER_ERROR.getCode(),"未提供足够的参数");
			}

			//保存订单信息
			PaymentTxn paymentTxn = paymentService.savePayment(orderNo, orderRequest.getPayType(), orderRequest.getRemark());
			OrderInfo orderInfo = new OrderInfo();
			PropertyUtils.copyProperties(orderInfo, orderRequest);
			orderInfo.setMerchantPayerNo(paymentTxn.getPayerNo());
			orderInfo.setPayeeNo("000000000000000000");//消费支付的订单收款方默认都是orders平台
			orderInfo.setMerchantNo("orders");
			orderInfo.setMerchantOrderNo(orderNo);
			orderInfo.setMerchantPayeeNo("orders");
			orderInfo.setAmt(paymentTxn.getAmt().divide(BigDecimal.valueOf(1000)));
			orderInfo.setOrderType(ThirdPayOrderDto.ORDER_TYPE_CONSUME);
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
		try{
			//改掉订单状态
			paymentService.payed(thirdPayOrderNo,Long.parseLong(bussinessNo));
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.addObject("order_id", "TestOrderId");
			modelAndView.setViewName("/payment/callback_success.html"); // 注意路径的正确性
			return modelAndView;
		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			//跳转向异常页面
			ModelAndView modelAndView = new ModelAndView();
			if(e instanceof AppException){
				AppException ae = (AppException) e;
				String errorMsg = ae.getErrorCode() + ":" + ae.getErrorMsg();
				modelAndView.addObject("errorMsg", errorMsg);
			}
			modelAndView.setViewName("/payment/callback_error.html"); // 注意路径的正确性
			return modelAndView;
		}
	}

	/**
	 * 支付平台处理失败，回调我们
	 * @return
	 */
	@RequestMapping(value="/callback_fail",method= {RequestMethod.GET})
	public ModelAndView callbackFail(@RequestParam(value = "code",required = false) String code,
									 @RequestParam(value = "msg",required = false)String msg){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("errorMsg", code+":"+msg);
		modelAndView.setViewName("/payment/callback_error.html"); // 注意路径的正确性
		return modelAndView;
	}

	@RequestMapping(value="/orderDetail",method= {RequestMethod.GET})
	public RespMsg orderDetail(@RequestParam("payOrderNo") String payOrderNo) {
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		payOrder.setSubOrders(null);//订单详情不需要返回子订单
		return new RespMsg(RespCode.SUCCESS,"success",payOrder);
	}

	/**
	 * 支付平台处理完成通知我们
	 * @param notifyInfo
	 * @return
	 */
	@RequestMapping(value="/notify",method= {RequestMethod.POST})
	public RespMsg notify(@RequestBody NotifyInfo notifyInfo) throws ServletException, IOException {
		if(RespMsg.SUCCESS.getCode().equals(notifyInfo.getResultCode())) {
			paymentService.notifyPaySuccess(notifyInfo.getMerchantOrderNo(), notifyInfo.getOrderNo(), Long.parseLong(notifyInfo.getBussinessNo()));
		}else {
			paymentService.notifyPayFail(notifyInfo.getMerchantOrderNo(), notifyInfo.getOrderNo(), Long.parseLong(notifyInfo.getBussinessNo()), notifyInfo.getResultCode(), notifyInfo.getMsg());
		}
		return RespMsg.SUCCESS;
	}

	/**
	 * 刷新订单状态
	 * @param payOrderNo
	 * @return
	 */
	@RequestMapping(value="/refreshOrder",method= {RequestMethod.POST})
	public RespMsg refreshOrder(String payOrderNo) {
		PayOrder payOrder = orederService.getPayOrder(payOrderNo);
		if(!PayOrder.ORDER_STATUS_PAYING.equals(payOrder.getStatus())){
			LOGGER.info("状态刷新只针对支付中的订单");
			return new RespMsg(RespCode.SUCCESS,"success",payOrder.getStatus());
		}

		PaymentTxn payment = paymentService.getPayingPaymentByPayOrderNo(payOrderNo);
		if (payment==null){
			LOGGER.info("payment 不处于");
			String newStatus = paymentService.checkOrderStatus(payOrderNo);
			return new RespMsg(RespCode.SUCCESS,"success",newStatus);
		}
		//第三方支付平台的订单状态
		String newStatus = paymentService.checkOrderStatusFromThirdPay(payment.getPaymentId());
		return new RespMsg(RespCode.SUCCESS,"success",newStatus);
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
