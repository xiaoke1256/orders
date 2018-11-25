package com.xiaoke1256.orders.search.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xiaoke1256.orders.search.service.EsCollectService;

@Service
public class ImportDataTask {
	@Autowired
	private EsCollectService esCollectService;
	
	public void collectProduct() {
		esCollectService.collectProduct();
	}
}
