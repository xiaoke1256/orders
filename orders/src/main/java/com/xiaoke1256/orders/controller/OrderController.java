package com.xiaoke1256.orders.controller;

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

import com.xiaoke1256.orders.bo.PayOrder;
import com.xiaoke1256.orders.bo.SubOrder;
import com.xiaoke1256.orders.service.OrederService;


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
	public @ResponseBody com.xiaoke1256.orders.vo.PayOrder orderDetail(@PathVariable("orderNo") String orderNo){
		System.out.println(orderNo);
		PayOrder order = orederService.getPayOrder(orderNo);
		if(order==null)
			return null;
		else
			return covertToVo(order);
		
	}
	
	@RequestMapping(value="/",method={RequestMethod.POST})
	public @ResponseBody com.xiaoke1256.orders.vo.PayOrder placeOrder(@RequestBody OrderPlaceRequest request){
		PayOrder order = orederService.place(request.getPayerNo(), request.getCarriageAmt(), request.getProductMap());
		return covertToVo(order);
	}
	
	private com.xiaoke1256.orders.vo.PayOrder covertToVo(PayOrder order){
		try{
			com.xiaoke1256.orders.vo.PayOrder orderVo = new com.xiaoke1256.orders.vo.PayOrder();
			orderVo.setPayerNo(order.getPayerNo());
			orderVo.setCarriageAmt(order.getCarriageAmt());
			orderVo.setInsertTime(order.getInsertTime());
			orderVo.setPayOrderNo(order.getPayOrderNo());
			orderVo.setTotalAmt(order.getTotalAmt());
			orderVo.setUpdateTime(order.getUpdateTime());
			if(order.getSubOrders()!=null){
				Set<com.xiaoke1256.orders.vo.SubOrder> subOrderSet = new LinkedHashSet<com.xiaoke1256.orders.vo.SubOrder>();
				for(SubOrder subOrder:order.getSubOrders()){
					com.xiaoke1256.orders.vo.SubOrder  subOrderVo = new com.xiaoke1256.orders.vo.SubOrder();
					PropertyUtils.copyProperties(subOrderVo, subOrder);
					subOrderVo.setProductId(subOrder.getProduct().getProductId());
					subOrderSet.add(subOrderVo);
				}
				orderVo.setSubOrders(subOrderSet);
			}
			return orderVo;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
}
