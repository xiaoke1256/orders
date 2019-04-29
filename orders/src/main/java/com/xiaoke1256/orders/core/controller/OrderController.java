package com.xiaoke1256.orders.core.controller;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.dto.PayOrderCondition;
import com.xiaoke1256.orders.core.dto.PayOrderQueryResultResp;
import com.xiaoke1256.orders.core.service.OrederService;


@Controller
@RequestMapping("/orders")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrederService orederService;
	
	@RequestMapping(value="/",method={RequestMethod.GET})
	public String toIndex(){
		return "orders/index";
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
			ErrMsg error = new ErrMsg(ex);
			return error;
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			ErrMsg error = new ErrMsg(ex);
			return error;
		}
	}
	
//	@RequestMapping(value="/search",method={RequestMethod.GET})
//	@ResponseBody
//	public PayOrderQueryResultResp searchOrders(PayOrderCondition condition) {
//		
//	}
	
}
