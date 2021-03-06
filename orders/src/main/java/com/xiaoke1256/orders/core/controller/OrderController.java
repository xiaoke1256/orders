package com.xiaoke1256.orders.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.core.bo.OrderItem;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.dto.OrderCondition;
import com.xiaoke1256.orders.core.dto.OrderQueryResultResp;
import com.xiaoke1256.orders.core.service.OrederService;


@Controller
@RequestMapping("/orders")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrederService orederService;
	
	@RequestMapping(value="/{orderNo}",method={RequestMethod.GET})
	@ResponseBody
	public com.xiaoke1256.orders.core.dto.SubOrder orderDetail(@PathVariable("orderNo") String orderNo){
		SubOrder order = orederService.getSubOrder(orderNo);
		return covertToVo(order);
	}
	
	@RequestMapping(value="/place",method={RequestMethod.POST})
	@ResponseBody
	public RespMsg placeOrder(@RequestBody OrderPlaceRequest request){
		try {
			PayOrder order = orederService.place(request.getPayerNo(), request.getProductMap());
			OrderPlaceResponse response = new OrderPlaceResponse();
			PropertyUtils.copyProperties(response, order);
			return response ;
		}catch(AppException ex){
			logger.error(ex.getMessage(), ex);
			RespMsg error = new RespMsg(ex);
			return error;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			RespMsg error = new RespMsg(ex);
			return error;
		}
	}
	
	/**
	 * 为了确认备货而进行的订单查询
	 * @param condition
	 * @return
	 */
	@RequestMapping(value="/searchForAccept",method={RequestMethod.GET})
	@ResponseBody
	public RespMsg searchOrdersForAccept(OrderCondition condition) {
		condition.setStatuses(new String[] {SubOrder.ORDER_STATUS_AWAIT_ACCEPT});
		QueryResult<SubOrder> queryResult = orederService.searchOrderByCondition(condition);
		List<com.xiaoke1256.orders.core.dto.SubOrder> voList = new ArrayList<com.xiaoke1256.orders.core.dto.SubOrder>();
		for(SubOrder subOrder:queryResult.getResultList()) {
			voList.add(covertToVo(subOrder));
		}
		QueryResult<com.xiaoke1256.orders.core.dto.SubOrder> voResult = new QueryResult<com.xiaoke1256.orders.core.dto.SubOrder>(queryResult.getPageNo(),queryResult.getPageSize(),queryResult.getTotalCount());
		voResult.setResultList(voList);
		return new OrderQueryResultResp(voResult);
	}
	
	/**
	 * 为了物流而进行的订单查询
	 * @param condition
	 * @return
	 */
	@RequestMapping(value="/searchForLo",method={RequestMethod.GET})
	@ResponseBody
	public RespMsg searchOrdersForLogistics(OrderCondition condition) {
		condition.setStatuses(new String[] {SubOrder.ORDER_STATUS_AWAIT_SEND,SubOrder.ORDER_STATUS_AWAIT_ACCEPT});
		QueryResult<SubOrder> queryResult = orederService.searchOrderByCondition(condition);
		List<com.xiaoke1256.orders.core.dto.SubOrder> voList = new ArrayList<com.xiaoke1256.orders.core.dto.SubOrder>();
		for(SubOrder subOrder:queryResult.getResultList()) {
			voList.add(covertToVo(subOrder));
		}
		QueryResult<com.xiaoke1256.orders.core.dto.SubOrder> voResult = new QueryResult<com.xiaoke1256.orders.core.dto.SubOrder>(queryResult.getPageNo(),queryResult.getPageSize(),queryResult.getTotalCount());
		voResult.setResultList(voList);
		return new OrderQueryResultResp(voResult);
	}
	
	
	/**
	 * 为了收货确认而进行的订单查询
	 * @param condition
	 * @return
	 */
	@RequestMapping(value="/searchForReceive",method={RequestMethod.GET})
	@ResponseBody
	public RespMsg searchOrdersForReceive(OrderCondition condition) {
		condition.setStatus(SubOrder.ORDER_STATUS_SENDING);
		QueryResult<SubOrder> queryResult = orederService.searchOrderByCondition(condition);
		List<com.xiaoke1256.orders.core.dto.SubOrder> voList = new ArrayList<com.xiaoke1256.orders.core.dto.SubOrder>();
		for(SubOrder subOrder:queryResult.getResultList()) {
			voList.add(covertToVo(subOrder));
		}
		QueryResult<com.xiaoke1256.orders.core.dto.SubOrder> voResult = new QueryResult<com.xiaoke1256.orders.core.dto.SubOrder>(queryResult.getPageNo(),queryResult.getPageSize(),queryResult.getTotalCount());
		voResult.setResultList(voList);
		return new OrderQueryResultResp(voResult);
	}
	
	/**
	 * 子订单转化成Vo
	 * @param subOrder
	 * @return
	 */
	private com.xiaoke1256.orders.core.dto.SubOrder covertToVo(SubOrder subOrder) {
		try {
			com.xiaoke1256.orders.core.dto.SubOrder subOrderVo = new com.xiaoke1256.orders.core.dto.SubOrder();
			String productCodes = "";
			PropertyUtils.copyProperties(subOrderVo, subOrder);
		
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
			subOrderVo.setProductCodes(productCodes);
			return subOrderVo;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
	
}
