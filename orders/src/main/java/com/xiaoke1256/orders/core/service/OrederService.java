package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.xiaoke1256.orders.core.bo.OStorage;
import com.xiaoke1256.orders.core.bo.OrderItem;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.product.dto.Product;

@Service
@Transactional
public class OrederService {
	private static  final Logger logger = LogManager.getLogger(OrederService.class);
//	@Autowired
//	private BaseDao baseDao;
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public PayOrder place(String payerNo,Map<String,Integer> orderMap){
		logger.info("start place a oreder.");
		if(orderMap.isEmpty())
			throw new RuntimeException("empty sub order.");
		
		//商品数量必须大于0
		for(Integer num:orderMap.values()) {
			if(num<=0) {
				throw new RuntimeException("product number must more than zero!");
			}
		}
			
		//先查各个商品的信息 ，判断是否是上线商品。
		List<Product> products = new ArrayList<Product>();
		for(Map.Entry<String,Integer> enty:orderMap.entrySet()) {
			String productCode = enty.getKey();
			Product product = restTemplate.getForObject("http:127.0.0.1:8081/product/product/"+productCode, Product.class);
			if(!"1".equals(product.getProductStatus())) {
				throw new RuntimeException("商品未上线。");
			}
			products.add(product);
		}
		
		//按店铺分组。
		Map<String, List<Product>> groupByStore = groupByStoreNo(products);
		
		//构造子订单
		List<SubOrder> subOrders = new ArrayList<SubOrder>();
		for(Entry<String, List<Product>> entry:groupByStore.entrySet()) {
			String storeNo =entry.getKey();
			List<Product> subProducts = entry.getValue();
			Map<Product,Integer> subOrderMap = new HashMap<Product,Integer>();
			for(Product product:subProducts) {
				subOrderMap.put(product, orderMap.get(product.getProductCode()));
			}
			//检查各个商品的运费定价。（同一店铺商品不超过1公斤的情况下，运费不会叠加）。
			BigDecimal carriage = calCarriage(storeNo,subOrderMap);
			//构造子订单
			SubOrder subOrder = new SubOrder();
			subOrder.setCarriageAmt(carriage);
			subOrder.setStoreNo(storeNo);
			//构造订单项
			Set<OrderItem> items = new HashSet<OrderItem>();
			for(Product product:subProducts) {
				OrderItem orderItem = new OrderItem();
				orderItem.setProductCode(product.getProductCode());
				orderItem.setProductNum(orderMap.get(product.getProductCode()));
				orderItem.setSubOrder(subOrder);
			}
			//subOrder.setTotalAmt(totalAmt);
			subOrder.setOrderItems(items);
			subOrders.add(subOrder);
		}
		
		//扣减库存
		for(Entry<String,Integer> enty:orderMap.entrySet()){
			String productId = enty.getKey();
			Integer num = enty.getValue();
			String hql = "update OStorage set stockNum = (stockNum- ?) where productCode = ? and stockNum>= ? ";
			int excResult = entityManager.createQuery(hql)
					.setParameter(1, num.longValue())
					.setParameter(2, productId)
					.setParameter(3, num.longValue())
					.executeUpdate();
			if(excResult<=0)
				throw new RuntimeException("库存不足");
		}
		entityManager.flush();
		//创建支付单
		PayOrder payOrder = createPayOrder(payerNo, subOrders);
		entityManager.persist(payOrder);
		entityManager.flush();
		return payOrder;
//		for(SubOrder subOrder:payOrder.getSubOrders()){
//			entityManager.persist(subOrder);
//		}
	}
	
	private PayOrder createPayOrder(String payerNo,List<SubOrder> subOrders){
		PayOrder payOrder = new PayOrder();
		payOrder.setPayerNo(payerNo);
		//计算订单总额
		BigDecimal totalAmt = BigDecimal.ZERO;
		for(SubOrder subOrder:subOrders) {
			totalAmt = totalAmt.add(subOrder.getTotalAmt());
		}
		payOrder.setTotalAmt(totalAmt );
		payOrder.setInsertTime(new Timestamp(System.currentTimeMillis()));
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		payOrder.setSubOrders(new LinkedHashSet<SubOrder>(subOrders));
		return payOrder;
	}
	
	@Transactional(readOnly=true)
	public PayOrder getPayOrder(String payOrderNo){
		PayOrder order = entityManager.find(PayOrder.class, payOrderNo);
		if(order!=null)
			Hibernate.initialize(order.getSubOrders());
		return order;
	}
	
	/**
	 * 按店铺分组
	 * @return 其key是店铺号。
	 */
	private Map<String,List<Product>> groupByStoreNo(List<Product> products){
		Map<String,List<Product>> resultMap = new LinkedHashMap<String,List<Product>>();
		for(Product product:products) {
			String productCode = product.getProductCode();
			List<Product> list = resultMap.get(productCode);
			if(list==null) {
				list = new ArrayList<Product>();
				resultMap.put(product.getStore().getStoreNo(), list);
			}
			list.add(product);
		}
		return resultMap;
	}
	
	/**
	 * 计算运费
	 * @param storeNo
	 * @param products
	 * @return
	 */
	private BigDecimal calCarriage(String storeNo, Map<Product,Integer> subOrderMap) {
		//算法先简单一点。
		//5件商品以内运费7元，5件以上按比例增加
		int productNum = subOrderMap.values().stream().mapToInt(x->x).sum();
		if(productNum<5) {
			return new BigDecimal(7000);
		}else {
			return new BigDecimal(7000).multiply(BigDecimal.valueOf(productNum)).divide(BigDecimal.valueOf(5), 3, RoundingMode.HALF_UP);
		}
		
	}
	
}
