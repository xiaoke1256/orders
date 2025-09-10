package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;
import java.util.List;

import com.xiaoke1256.orders.product.bo.StoreMember;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xiaoke1256.orders.product.api.StoreQueryService;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreController implements com.xiaoke1256.orders.product.api.StoreService {
	
	@Autowired
	private StoreService storeService;

	/*
	 * (non-Javadoc)
	 * @see com.xiaoke1256.orders.product.api.StoreQueryService#queryAvailableStore()
	 */
	@RequestMapping(value="/queryAvailable",method=RequestMethod.GET)
	@Override
	public List<Store> queryAvailableStore() {
		List<com.xiaoke1256.orders.product.domain.Store> bos = storeService.queryAvailableStores();
		List<Store> dtos = new ArrayList<Store>();
		for(com.xiaoke1256.orders.product.domain.Store domain:bos) {
			Store dto = new Store();
			BeanUtils.copyProperties(domain, dto);
			dtos.add(dto);
		}
		return dtos;
	}
	
	@RequestMapping(value="/{storeNo}",method=RequestMethod.GET)
	@Override
	public Store getStore(@PathVariable("storeNo") String storeNo) {
		com.xiaoke1256.orders.product.domain.Store domain = storeService.getByStoreNo(storeNo);
		if(domain == null) {
			return null;
		}
		Store dto = new Store();
		BeanUtils.copyProperties(domain, dto);
		return dto;
	}

	@RequestMapping(method=RequestMethod.POST)
	public void createStore(@RequestBody Store store,@RequestParam String leaderAccount){
		com.xiaoke1256.orders.product.domain.Store domain = new com.xiaoke1256.orders.product.domain.Store();
		BeanUtils.copyProperties(store,domain);
		storeService.createStore(domain,leaderAccount);
	}

	@RequestMapping(method=RequestMethod.PUT)
	public void updateStore(@RequestBody Store store){
		com.xiaoke1256.orders.product.domain.Store domain = new com.xiaoke1256.orders.product.domain.Store();
		BeanUtils.copyProperties(store,domain);
		storeService.updateStore(domain);
	}

}
