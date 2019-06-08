package com.xiaoke1256.thirdpay.payplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiaoke1256.orders.common.zookeeper.Worker;

@Component
public class ExpiredWorker extends Worker {
	
	@Autowired
	private ThirdPayService thirdPayService;

	public ExpiredWorker() {
		super("/zookeeper/3rdpay/expired_order");
	}

	@Override
	protected void doBusiness(String businessData) {
		String orderNo = (String)businessData;
		thirdPayService.expired(orderNo );
	}

}
