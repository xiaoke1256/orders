package com.xiaoke1256.orders.core.controller;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.core.bo.OrderItem;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.service.OrederService;


@Controller
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrederService orederService;
	
	@RequestMapping(value="/",method={RequestMethod.GET})
	public String toIndex(){
		return "orders/index";
	}
	
	@RequestMapping(value="/{orderNo}",method={RequestMethod.GET})
	public @ResponseBody com.xiaoke1256.orders.core.dto.PayOrder orderDetail(@PathVariable("orderNo") String orderNo){
		//System.out.println(orderNo);
		PayOrder order = orederService.getPayOrder(orderNo);
		if(order==null)
			return null;
		else
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
		}catch(Exception ex){
			ErrMsg error = new ErrMsg("code",ex.getMessage());
			return error;
		}
	}
	
	private com.xiaoke1256.orders.core.dto.PayOrder covertToVo(PayOrder order){
		try{
			com.xiaoke1256.orders.core.dto.PayOrder orderVo = new com.xiaoke1256.orders.core.dto.PayOrder();
			orderVo.setPayerNo(order.getPayerNo());
			orderVo.setCarriageAmt(order.getCarriageAmt());
			orderVo.setInsertTime(order.getInsertTime());
			orderVo.setTotalAmt(order.getTotalAmt());
			orderVo.setUpdateTime(order.getUpdateTime());
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
					}
					subOrderVo.setOrderItems(orderItemSet);
				}
				orderVo.setSubOrders(subOrderSet);
			}
			return orderVo;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
}
