package com.xiaoke1256.orders.pay.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.client.ThirdPaymentClient;
import com.xiaoke1256.thirdpay.payplatform.dto.PayRequest;
import com.xiaoke1256.thirdpay.payplatform.dto.PayResp;

/**
 * 支付方法（一般的支付与业务没有关系）
 * @author Administrator
 *
 */
@Service
public class PayService {
	private static final Logger logger = LoggerFactory.getLogger(PayService.class);

	@Autowired
	private ThirdPaymentClient thirdPaymentClient;
	
	/**
	 * 
	 * @param payerNo 付款方账号
	 * @param payeeNo 收款方账号
	 * @param amt 金额
	 * @param orderType 订单类型（01-消费;02-退货款;03-与平台方结算;04-理财;05-结息;06-借款;07-还款;99-其他）
	 * @param palteform 第三方平台（统一写 orders）
	 * @param remark 备注
	 */
	public void pay(String payerNo,String payeeNo,BigDecimal amt,String orderType,String palteform,String remark) {
		PayRequest payRequest = new PayRequest();
		payRequest.setPayerNo(payerNo);
		payRequest.setPayeeNo(payeeNo);
		payRequest.setAmt(amt);
		payRequest.setOrderType(orderType);
		payRequest.setPalteform(palteform);
		payRequest.setRemark(remark);
		PayResp resp = thirdPaymentClient.pay(payRequest );
		if(!RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
			throw new AppException(resp.getCode(),resp.getMsg());
		}
		String orderNo = resp.getOrderNo();
		String verifyCode = resp.getVerifyCode();
		
		try {
			//TODO 处理notice 业务。
			try {
				//处理完了后通知第三方平台。
				RespMsg noticeResp = thirdPaymentClient.acceptNote(orderNo, "SUCCESS");
				if(!RespMsg.SUCCESS.getCode().equals(resp.getCode())) {
					throw new RemoteException(resp.getCode()+":"+resp.getMsg());
				}
			}catch(RestClientException ex ) {
				throw new RemoteException(ex.getMessage(),ex);
			}			
			
		}catch(RemoteException ex) {
			logger.error(ex.getMessage(),ex);
			//远程调用异常要将支付取消
			try {
				//TODO cancel(canelRequest );
			} catch(Exception e) {
				logger.error(e.getMessage(),e);
			}
			throw new RuntimeException(ex);
		}catch(AppException ex){
			//失败也要通知第三方支付平台
			logger.error(ex.getMessage(),ex);
			thirdPaymentClient.acceptNote(orderNo, "FAIL");
			throw ex;
		}catch(Exception ex){
			//失败也要通知第三方支付平台
			logger.error(ex.getMessage(),ex);
			thirdPaymentClient.acceptNote(orderNo, "FAIL");
			throw new RuntimeException(ex);
		}
		
	}
}
