package com.xiaoke1256.orders.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.core.api.MakeMoneyService;
import com.xiaoke1256.orders.core.bo.SettleStatemt;
import com.xiaoke1256.orders.core.client.StoreQueryClient;
import com.xiaoke1256.orders.core.dto.PayResp;
import com.xiaoke1256.orders.core.dto.SettleStatemtCondition;
import com.xiaoke1256.orders.core.dto.SettleStatemtQueryResultResp;
import com.xiaoke1256.orders.core.service.SettleService;
import com.xiaoke1256.orders.pay.service.PayService;
import com.xiaoke1256.orders.product.dto.Store;

/**
 * 打款相关的业务
 * @author Administrator
 *
 */
@RestController
public class MakeMoneyController implements MakeMoneyService {
	
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
	@RequestMapping(value="/makeMoney/pay",method= {RequestMethod.POST})
	public PayResp makeMoney(@RequestBody String settleNo) {
		//检查
		if(StringUtils.isEmpty(settleNo)) {
			return new PayResp(RespCode.EMPTY_PARAMTER_ERROR);
		}
		SettleStatemt settle = settleService.getSettleStatemtByNo(settleNo);
		if(settle==null) {
			return new PayResp(RespCode.WRONG_PARAMTER_ERROR,"wrong settleNo!");
		}
		//检查结算单状态。
		if(!SettleStatemt.STATUS_AWAIT_MAKE_MONEY.equals(settle.getStatus())) {
			return new PayResp(RespCode.STATUS_ERROR,String.format("SettleStatemt(no:%s) in wrong status.", settleNo));
		}
		
		Date now = new Date();
		Date lastMonth = DateUtil.addMonth(now, -1);
		if(Integer.parseInt(settle.getMonth())!=DateUtil.getMonth(lastMonth)
				||Integer.parseInt(settle.getYear())!=DateUtil.getYear(lastMonth)) {
			return new PayResp(RespCode.BUSSNESS_ERROR,"仅能对上一个月的结算单进行打款.");
		}
		
		String storeNo = settle.getStoreNo();
		//调用product服务，查询商铺的收款账号和支付方式。
		Store store = storeQueryClient.getStore(storeNo);
		String payerNo = pateformPayAccount;//付款方
		String payeeNo = store.getPayAccountNo();//收款方
		//String payType = store.getPayType();//支付方式
		BigDecimal amt = settle.getPendingPayment();
		//TODO 假装根据 payType 找到3rdPay的service.
		String orderType = "03";//03表示“与平台方结算”
		String palteform = "orders";
		String remark = settleNo;
		payService.pay(payerNo, payeeNo, amt, orderType, palteform , remark );
		return new PayResp(RespCode.SUCCESS);
		
	}
	
	@RequestMapping(value="/settles/search",method= {RequestMethod.GET})
	public SettleStatemtQueryResultResp searchSettle(SettleStatemtCondition condition) {
		QueryResult<SettleStatemt> queryResult = settleService.searchSettleStatemts(condition);
		List<com.xiaoke1256.orders.core.dto.SettleStatemt> voList = new ArrayList<com.xiaoke1256.orders.core.dto.SettleStatemt>();
		for(SettleStatemt settleStatemt:queryResult.getResultList()) {
			voList.add(covertToVo(settleStatemt));
		}
		QueryResult<com.xiaoke1256.orders.core.dto.SettleStatemt> voResult = new QueryResult<com.xiaoke1256.orders.core.dto.SettleStatemt>(queryResult.getPageNo(),queryResult.getPageSize(),queryResult.getTotalCount());
		voResult.setResultList(voList);
		return new SettleStatemtQueryResultResp(voResult);
	}
	
	/**
	 * 查询待打款的结算单
	 */
	@RequestMapping(value="/settles/queryAwaitMakeMoney",method= {RequestMethod.GET})
	public List<com.xiaoke1256.orders.core.dto.SettleStatemt> queryAwaitMakeMoney(@RequestParam("year") String year,@RequestParam("month") String month) {
		SettleStatemtCondition condition = new SettleStatemtCondition();
		condition.setYear(year);
		condition.setMonth(month);
		condition.setStatus(SettleStatemt.STATUS_AWAIT_MAKE_MONEY);
		List<SettleStatemt> result = settleService.querySettleStatemts(condition);
		List<com.xiaoke1256.orders.core.dto.SettleStatemt> voList = new ArrayList<com.xiaoke1256.orders.core.dto.SettleStatemt>();
		for(SettleStatemt settleStatemt:result) {
			voList.add(covertToVo(settleStatemt));
		}
		return voList;
	}

	private com.xiaoke1256.orders.core.dto.SettleStatemt covertToVo(SettleStatemt bo) {
		try {
			com.xiaoke1256.orders.core.dto.SettleStatemt dto = new com.xiaoke1256.orders.core.dto.SettleStatemt();
			PropertyUtils.copyProperties(dto, bo);
			return dto;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
