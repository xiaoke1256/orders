package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreQueryController implements StoreQueryService {
	
	@Autowired
	private StoreService storeService;

	/*
	 * (non-Javadoc)
	 * @see com.xiaoke1256.orders.product.api.StoreQueryService#queryAvailableStore()
	 */
	@RequestMapping(value="/queryAvailable",method=RequestMethod.GET)
	@Override
	public List<Store> queryAvailableStore() {
		List<com.xiaoke1256.orders.product.bo.Store> bos = storeService.queryAvailableStores();
		List<Store> dtos = new ArrayList<Store>();
		for(com.xiaoke1256.orders.product.bo.Store bo:bos) {
			Store dto = new Store();
			BeanUtils.copyProperties(bo, dto);
			dtos.add(dto);
		}
		return dtos;
	}
	
	

}
