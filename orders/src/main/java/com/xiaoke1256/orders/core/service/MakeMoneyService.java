package com.xiaoke1256.orders.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.pay.service.PayBusinessService;

/**
 * 处理打款业务
 * @author Administrator
 *
 */
@Service
@Transactional
public class MakeMoneyService implements PayBusinessService {

	@Override
	public void notice(String orderNo, String payType, String remark) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancel(String orderNo, String cancelType, String remark) {
		// TODO Auto-generated method stub
		
	}

}
