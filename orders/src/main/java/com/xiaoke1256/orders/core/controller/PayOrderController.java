package com.xiaoke1256.orders.core.controller;

import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.core.bo.OrderItem;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.dto.PayOrderCondition;
import com.xiaoke1256.orders.core.dto.PayOrderQueryResultResp;
import com.xiaoke1256.orders.core.service.OrederService;

@Controller
@RequestMapping("/payOrders")
public class PayOrderController {
	@Autowired
	private OrederService orederService;
	
	@RequestMapping(value="/{orderNo}",method={RequestMethod.GET})
	public @ResponseBody com.xiaoke1256.orders.core.dto.PayOrder orderDetail(@PathVariable("orderNo") String orderNo){
		//System.out.println(orderNo);
		PayOrder order = orederService.getPayOrder(orderNo);
		if(order==null)
			return null;
		else
			return covertToVo(order);
		
	}
	
	private com.xiaoke1256.orders.core.dto.PayOrder covertToVo(PayOrder order){
		try{
			com.xiaoke1256.orders.core.dto.PayOrder orderVo = new com.xiaoke1256.orders.core.dto.PayOrder();
			String productCodes = "";
			orderVo.setPayerNo(order.getPayerNo());
			orderVo.setCarriageAmt(order.getCarriageAmt());
			orderVo.setInsertTime(order.getInsertTime());
			orderVo.setTotalAmt(order.getTotalAmt());
			orderVo.setUpdateTime(order.getUpdateTime());
			orderVo.setPayOrderNo(order.getPayOrderNo());
			orderVo.setStatus(order.getStatus());
			if(order.getSubOrders()!=null){
				Set<com.xiaoke1256.orders.core.dto.SubOrder> subOrderSet = new LinkedHashSet<com.xiaoke1256.orders.core.dto.SubOrder>();
				for(SubOrder subOrder:order.getSubOrders()){
					com.xiaoke1256.orders.core.dto.SubOrder  subOrderVo = new com.xiaoke1256.orders.core.dto.SubOrder();
					PropertyUtils.copyProperties(subOrderVo, subOrder);
					subOrderSet.add(subOrderVo);
					Set<com.xiaoke1256.orders.core.dto.OrderItem> orderItemSet = new LinkedHashSet<com.xiaoke1256.orders.core.dto.OrderItem>();
					for(OrderItem orderItem:subOrder.getOrderItems()) {
						com.xiaoke1256.orders.core.dto.OrderItem orderItemVo = new com.xiaoke1256.orders.core.dto.OrderItem();
						PropertyUtils.copyProperties(orderItemVo, orderItem);
						orderItemSet.add(orderItemVo);
						if(productCodes.length()>0)
							productCodes += ",";
						productCodes += orderItem.getProductCode();
					}
					subOrderVo.setOrderItems(orderItemSet);
				}
				orderVo.setSubOrders(subOrderSet);
			}
			orderVo.setProductCodes(productCodes);
			return orderVo;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * 搜索支付单
	 * @param condition
	 * @return
	 */
	@RequestMapping(value="/search",method={RequestMethod.GET})
	@ResponseBody
	public PayOrderQueryResultResp searchOrders(PayOrderCondition condition) {
		//condition.setStatus(PayOrder.ORDER_STATUS_INIT);//0是待支付
		condition.setStatuses(Arrays.asList(PayOrder.ORDER_STATUS_INIT,PayOrder.ORDER_STATUS_PAYING));
		QueryResult<PayOrder> queryResult = orederService.searchPayOrderByCondition(condition);
		List<com.xiaoke1256.orders.core.dto.PayOrder> voList = new ArrayList<com.xiaoke1256.orders.core.dto.PayOrder>();
		for(PayOrder payOrder:queryResult.getResultList()) {
			voList.add(covertToVo(payOrder));
		}
		QueryResult<com.xiaoke1256.orders.core.dto.PayOrder> voResult = new QueryResult<com.xiaoke1256.orders.core.dto.PayOrder>(queryResult.getPageNo(),queryResult.getPageSize(),queryResult.getTotalCount());
		voResult.setResultList(voList);
		return new PayOrderQueryResultResp(voResult);
	}
}
