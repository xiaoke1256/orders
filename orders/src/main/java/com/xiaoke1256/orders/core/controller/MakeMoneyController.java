package com.xiaoke1256.orders.core.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.bo.SettleStatemt;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.client.StoreQueryClient;
import com.xiaoke1256.orders.core.service.OrederService;
import com.xiaoke1256.orders.core.service.SettleService;
import com.xiaoke1256.orders.pay.service.PayService;
import com.xiaoke1256.orders.product.dto.Store;

/**
 * 打款相关的业务
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/makeMoney")
public class MakeMoneyController {
	
	@Autowired
	private PayService payService;
	
	@Autowired
	private SettleService settleService;
	
	@Autowired
	private StoreQueryClient storeQueryClient;
	
	/**平台方的支付账号*/
	@Value("${platform.payment.thirdpay.account}")
	private String pateformPayAccount ;
	
	/**
	 * 打款
	 * @param orderNo
	 * @return
	 */
	public RespMsg makeMoney(String settleNo) {
		//检查
		if(StringUtils.isEmpty(settleNo)) {
			return new RespMsg(RespCode.EMPTY_PARAMTER_ERROR);
		}
		SettleStatemt settle = settleService.getSettleStatemtByNo(settleNo);
		if(settle==null) {
			return new RespMsg(RespCode.WRONG_PARAMTER_ERROR,"wrong settleNo!");
		}
		//检查结算单状态。
		if(SettleStatemt.STATUS_AWAIT_MAKE_MONEY.equals(settle.getStatus())) {
			return new RespMsg(RespCode.STATUS_ERROR,String.format("SettleStatemt(no:%s) in wrong status.", settleNo));
		}
		String storeNo = settle.getStoreNo();
		//调用product服务，查询商铺的收款账号和支付方式。
		Store store = storeQueryClient.getStore(storeNo);
		String payerNo = pateformPayAccount;//付款方
		String payeeNo = store.getPayAccountNo();//收款方
		String payType = store.getPayType();//支付方式
		BigDecimal amt = settle.getPendingPayment();
		//TODO 假装根据 payType 找到3rdPay的service.
		String orderType = "03";//03表示“与平台方结算”
		String palteform = "orders";
		String remark = settleNo;
		payService.pay(payerNo, payeeNo, amt, orderType, palteform , remark );
		return RespMsg.SUCCESS;
		
	}
}
