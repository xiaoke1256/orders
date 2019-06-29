package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.StoreService;

@RestController
@RequestMapping("/store")
@Service
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
	
	@RequestMapping(value="/{storeNo}",method=RequestMethod.GET)
	@Override
	public Store getStore(@PathVariable("storeNo") String storeNo) {
		com.xiaoke1256.orders.product.bo.Store bo = storeService.getByStoreNo(storeNo);
		if(bo == null) {
			return null;
		}
		Store dto = new Store();
		BeanUtils.copyProperties(bo, dto);
		return dto;
	}
	
	

}
