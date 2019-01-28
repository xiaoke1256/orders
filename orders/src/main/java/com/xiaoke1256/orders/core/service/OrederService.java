package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;

@Service
@Transactional
public class OrederService {
	private static  final Logger logger = LogManager.getLogger(OrederService.class);
//	@Autowired
//	private BaseDao baseDao;
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	@Autowired
	private NoTransOrederService noTransOrederService;
	
	public PayOrder place(String payerNo,Map<String,Integer> productMap){
		logger.info("start place a oreder.");
		//productMap = new TreeMap<String,Integer>(productMap);
		if(productMap.isEmpty())
			throw new RuntimeException("empty sub order.");
			
		//先查各个商品的信息 ，判断是否是上线商品。
		
		//检查各个商品的运费定价。
		
		int index = 0;
		for(Entry<String,Integer> enty:productMap.entrySet()){
			String productId = enty.getKey();
			Integer num = enty.getValue();
			if(index>=productMap.size()-1){
				//boolean success = noTransOrederService.modiyProductStore(productId, num);
				boolean success = noTransOrederService.modiyProductStoreWithTrans(productId, num);
				if(!success)
					throw new RuntimeException("库存不足");
			}else{
				OStorage p = entityManager.find(OStorage.class, productId,LockModeType.PESSIMISTIC_WRITE);
				if(p.getStockNum()<num){
					throw new RuntimeException("库存不足");
				}
				p.setStockNum(p.getStockNum()-num);
				entityManager.merge(p);
			}
			index++;
		}
		entityManager.flush();
		PayOrder payOrder = createPayOrder(payerNo, productMap);
		entityManager.persist(payOrder);
		entityManager.flush();
		return payOrder;
//		for(SubOrder subOrder:payOrder.getSubOrders()){
//			entityManager.persist(subOrder);
//		}
	}
	
	private PayOrder createPayOrder(String payerNo,Map<String,Integer> productMap){
		PayOrder payOrder = new PayOrder();
		payOrder.setPayerNo(payerNo);
		//payOrder.setCarriageAmt(carriageAmt);
		//payOrder.setPayOrderNo(UUID.randomUUID().toString().substring(0, 22));
		payOrder.setInsertTime(new Timestamp(System.currentTimeMillis()));
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		Set<SubOrder> suborderSet = new HashSet<SubOrder>();
		for(Entry<String,Integer> entry:productMap.entrySet()){
			SubOrder subOrder = new SubOrder();
			subOrder.setPayOrder(payOrder);
			OStorage p = entityManager.find(OStorage.class,entry.getKey());
			//subOrder.setProduct(p);
			//subOrder.setProductNum(entry.getValue());
			//subOrder.setProductPrice(p.getProductPrice());
			//subOrder.setStoreNo(p.getStoreNo());
			suborderSet.add(subOrder);
		}
		payOrder.setSubOrders(suborderSet);
		return payOrder;
	}
	
	@Transactional(readOnly=true)
	public PayOrder getPayOrder(String payOrderNo){
		PayOrder order = entityManager.find(PayOrder.class, payOrderNo);
		if(order!=null)
			Hibernate.initialize(order.getSubOrders());
		return order;
	}
	
}
