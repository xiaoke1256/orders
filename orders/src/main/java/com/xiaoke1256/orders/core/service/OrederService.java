package com.xiaoke1256.orders.core.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.common.util.Base32;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.core.bo.OrderItem;
import com.xiaoke1256.orders.core.bo.PayOrder;
import com.xiaoke1256.orders.core.bo.SubOrder;
import com.xiaoke1256.orders.core.client.ProductQueryClient;
import com.xiaoke1256.orders.core.dto.OrderCondition;
import com.xiaoke1256.orders.core.dto.PayOrderCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;

@Service
@Transactional
public class OrederService {
	private static  final Logger logger = LoggerFactory.getLogger(OrederService.class);

	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	@Autowired
	private ProductQueryClient productQueryClient;
	
	@Value("${remote.api.product.uri}")
	private String productApiUri;
	
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
		List<SimpleProduct> products = new ArrayList<SimpleProduct>();
		for(Map.Entry<String,Integer> enty:orderMap.entrySet()) {
			String productCode = enty.getKey();
			SimpleProduct product = productQueryClient.getSimpleProductByCode(productCode);
			if(!"1".equals(product.getProductStatus())) {
				throw new RuntimeException("商品未上线。");
			}
			products.add(product);
		}

		//扣减库存，看库存是否足够
		for(Entry<String,Integer> enty:orderMap.entrySet()){
			String productId = enty.getKey();
			Integer num = enty.getValue();
			String hql = "update OStorage set stockNum = (stockNum- :num) where productCode = :productCode and stockNum>= :num ";
			int excResult = entityManager.createQuery(hql)
					.setParameter("num", num.longValue())
					.setParameter("productCode", productId)
					.executeUpdate();
			if(excResult<=0)
				throw new RuntimeException("库存不足");
		}

		//按店铺分组。
		Map<String, List<SimpleProduct>> groupByStore = groupByStoreNo(products);
		
		//构造子订单
		List<SubOrder> subOrders = new ArrayList<SubOrder>();
		for(Entry<String, List<SimpleProduct>> entry:groupByStore.entrySet()) {
			String storeNo =entry.getKey();
			List<SimpleProduct> subProducts = entry.getValue();
			Map<SimpleProduct,Integer> subOrderMap = new HashMap<SimpleProduct,Integer>();
			for(SimpleProduct product:subProducts) {
				subOrderMap.put(product, orderMap.get(product.getProductCode()));
			}
			//检查各个商品的运费定价。（同一店铺商品不超过1公斤的情况下，运费不会叠加）。
			BigDecimal carriage = calCarriage(storeNo,subOrderMap);
			//构造子订单
			SubOrder subOrder = new SubOrder();
			subOrder.setInsertTime(new Timestamp(System.currentTimeMillis()));
			subOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			subOrder.setCarriageAmt(carriage);
			subOrder.setStoreNo(storeNo);
			BigDecimal totalAmt = BigDecimal.ZERO.add(carriage);
			//构造订单项
			Set<OrderItem> items = new HashSet<OrderItem>();
			for(SimpleProduct product:subProducts) {
				OrderItem orderItem = new OrderItem();
				orderItem.setProductCode(product.getProductCode());
				orderItem.setProductNum(orderMap.get(product.getProductCode()));
				orderItem.setProductPrice(product.getProductPrice());
				orderItem.setSubOrder(subOrder);
				totalAmt = totalAmt.add(product.getProductPrice().multiply(BigDecimal.valueOf(orderMap.get(product.getProductCode()))));
				items.add(orderItem);
			}
			subOrder.setTotalAmt(totalAmt);
			subOrder.setOrderItems(items);
			subOrders.add(subOrder);
		}

		//创建支付单
		PayOrder payOrder = createPayOrder(payerNo, subOrders);
		entityManager.persist(payOrder);
		entityManager.flush();

		//保存子订单
		for(SubOrder subOrder:subOrders){
			Set<OrderItem> orderItems = subOrder.getOrderItems();
			subOrder.setOrderItems(null);
			entityManager.persist(subOrder);
			//保存订单项
			for(OrderItem item:orderItems) {
				item.setPayOrderId(payOrder.getPayOrderId());
				entityManager.merge(item);
			}
		}
		entityManager.flush();
		return payOrder;
	}
	
	private PayOrder createPayOrder(String payerNo,List<SubOrder> subOrders){
		PayOrder payOrder = new PayOrder();
		payOrder.setPayerNo(payerNo);
		//计算订单总额
		BigDecimal totalAmt = BigDecimal.ZERO;
		for(SubOrder subOrder:subOrders) {
			totalAmt = totalAmt.add(subOrder.getTotalAmt());
		}
		for(SubOrder subOrder:subOrders) {
			//生成订单号
			subOrder.setOrderNo(genOrderNo(subOrder));
			//维护父子关系
			subOrder.setPayOrder(payOrder);
		}
		payOrder.setTotalAmt(totalAmt );
		payOrder.setInsertTime(new Timestamp(System.currentTimeMillis()));
		payOrder.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		payOrder.setPayOrderNo(genPayOrderNo(payerNo));
		//payOrder.setSubOrders(new LinkedHashSet<SubOrder>(subOrders));
		return payOrder;
	}
	
	@Transactional(readOnly=true)
	public PayOrder getPayOrder(String payOrderNo){
		String hql = "from PayOrder where payOrderNo = :payOrderNo ";
		Query query = entityManager.createQuery(hql).setParameter("payOrderNo", payOrderNo);
		@SuppressWarnings("unchecked")
		List<PayOrder> list = query.getResultList();
		if(list==null||list.size()==0)
			return null;
		PayOrder order = list.get(0);
		return order;
	}
	
	/**
	 * 按店铺分组
	 * @return 其key是店铺号。
	 */
	private Map<String,List<SimpleProduct>> groupByStoreNo(List<SimpleProduct> products){
		Map<String,List<SimpleProduct>> resultMap = new LinkedHashMap<String,List<SimpleProduct>>();
		for(SimpleProduct product:products) {
			String productCode = product.getProductCode();
			List<SimpleProduct> list = resultMap.get(productCode);
			if(list==null) {
				list = new ArrayList<SimpleProduct>();
				resultMap.put(product.getStoreNo(), list);
			}
			list.add(product);
		}
		return resultMap;
	}
	
	/**
	 * 计算运费
	 * @param storeNo
	 * @param subOrderMap
	 * @return
	 */
	private BigDecimal calCarriage(String storeNo, Map<SimpleProduct,Integer> subOrderMap) {
		//算法先简单一点。
		//5件商品以内运费7元，5件以上按比例增加
		int productNum = subOrderMap.values().stream().mapToInt(x->x).sum();
		if(productNum<5) {
			return new BigDecimal(7000);
		}else {
			return new BigDecimal(7000).multiply(BigDecimal.valueOf(productNum)).divide(BigDecimal.valueOf(5), 3, RoundingMode.HALF_UP);
		}
		
	}
	
	/**
	 * 生成订单号
	 * 订单号规则:商铺号HashCode后转32进制数的后两位+年份后两位转成32进制数+16进制表示的月份+32进制表示的日
	 * +32进制表示的时+两位十进制表示的分+两位十进制表示的秒+3位十进制表示的微秒+2位32进制随机数。共15位。
	 * @param subOrder
	 * @return
	 */
	private String genOrderNo(SubOrder subOrder) {
		StringBuilder orderNo = new StringBuilder();
		
		String storeNoCode =Base32.encode(subOrder.getStoreNo().hashCode());
		if(storeNoCode.length()>2)
			storeNoCode = storeNoCode.substring(storeNoCode.length()-2);
		else if(storeNoCode.length()==1)
			storeNoCode = "0"+storeNoCode;
		
		orderNo.append(storeNoCode);
		
		Date now = new Date();
		int year = DateUtil.getYear(now);
		year = year%100%32;
		int month = DateUtil.getMonth(now);
		int date = DateUtil.getDate(now);
		int hour =DateUtil.getHour(now);
		int minute = DateUtil.getMinute(now);
		int second = DateUtil.getSecond(now);
		
		orderNo.append(Base32.encode(year))
			.append(Base32.encode(month))
			.append(Base32.encode(date))
			.append(Base32.encode(hour))
			.append(StringUtils.leftPad(String.valueOf(minute), 2, '0'))
			.append(StringUtils.leftPad(String.valueOf(second), 2, '0'));
		
		long nanoSecode = System.nanoTime();//这是纳秒，需要转成微妙。
		int microSecond = (int)((nanoSecode/1000)%1000);
		orderNo.append(StringUtils.leftPad(String.valueOf(microSecond), 3, '0'));
		
		orderNo.append(StringUtils.leftPad(Base32.encode(RandomUtils.nextInt(32*32)),2,'0'));
		return orderNo.toString();
	}
	
	/**
	 * 支付单号
	 * @param payerNo
	 * @return
	 */
	private String genPayOrderNo(String payerNo) {
		StringBuilder orderNo = new StringBuilder();
		String payereNoCode =Base32.encode(payerNo.hashCode());
		if(payereNoCode.length()>2)
			payereNoCode=payereNoCode.substring(payereNoCode.length()-2);
		else if(payereNoCode.length()==1)
			payereNoCode = "0"+payereNoCode;
		orderNo.append(payereNoCode);
		
		Date now = new Date();
		int year = DateUtil.getYear(now);
		year = year%100;
		int month = DateUtil.getMonth(now);
		int date = DateUtil.getDate(now);
		int hour =DateUtil.getHour(now);
		int minute = DateUtil.getMinute(now);
		int second = DateUtil.getSecond(now);
		int millisecond = DateUtil.getMillisecond(now);
		
		orderNo.append(StringUtils.leftPad(String.valueOf(year),2,'0'))
			.append(StringUtils.leftPad(String.valueOf(month),2,'0'))
			.append(StringUtils.leftPad(String.valueOf(date),2,'0'))
			.append(StringUtils.leftPad(String.valueOf(hour),2,'0'))
			.append(StringUtils.leftPad(String.valueOf(minute), 2, '0'))
			.append(StringUtils.leftPad(String.valueOf(second), 2, '0'))
			.append(StringUtils.leftPad(String.valueOf(millisecond), 3, '0'))
			.append(Base32.encode(RandomUtils.nextInt(32)));
		return orderNo.toString();
	}
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	@Transactional(readOnly=true)
	public QueryResult<PayOrder> searchPayOrderByCondition(PayOrderCondition condition){
		String countQl = "";
		StringBuilder hqlSb =new StringBuilder(" from PayOrder o where 1=1 ");
		Map<String,Object> paramMap = new HashMap<>();
		if(!StringUtils.isEmpty(condition.getPayerNo())) {
			hqlSb.append(" and payerNo like :payerNo");
			paramMap.put("payerNo", condition.getPayerNo()+"%");
		}
		if(!StringUtils.isEmpty(condition.getPayOrderNo())) {
			hqlSb.append(" and payOrderNo like :payOrderNo");
			paramMap.put("payOrderNo", condition.getPayOrderNo()+"%");
		}
		if(!StringUtils.isEmpty(condition.getStatus())) {
			hqlSb.append(" and status = :status");
			paramMap.put("status", condition.getStatus());
		}
		if(condition.getStatuses()!=null && condition.getStatuses().size()>0){
			hqlSb.append(" and status in (:statuses)");
			paramMap.put("statuses", condition.getStatuses());
		}
		countQl= "select count(o) "+hqlSb.toString();
		hqlSb.append(" order by payOrderId ");
		
		Query countQuery = entityManager.createQuery(countQl);
		for(String key:paramMap.keySet()) {
			countQuery.setParameter(key, paramMap.get(key));
		}
		int totalCount = ((Number)countQuery.getSingleResult()).intValue();

		Query query = entityManager.createQuery(hqlSb.toString());
		for(String key:paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		
		QueryResult<PayOrder> page = new QueryResult<PayOrder>(condition.getPageNo(),condition.getPageSize(),totalCount);
		
		query.setFirstResult((condition.getPageNo()-1)*condition.getPageSize());
		query.setMaxResults(condition.getPageSize());
		@SuppressWarnings("unchecked")
		List<PayOrder> results = query.getResultList();
		page.setResultList(results);
		
		return page;
	}
	
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	@Transactional(readOnly=true)
	public QueryResult<SubOrder> searchOrderByCondition(OrderCondition condition){
		String countQl = "";
		StringBuilder hqlSb =new StringBuilder(" from SubOrder o where 1=1 ");
		Map<String,Object> paramMap = new HashMap<>();
		if(!StringUtils.isEmpty(condition.getStoreNo())) {
			hqlSb.append(" and storeNo like :storeNo");
			paramMap.put("storeNo", condition.getStoreNo()+"%");
		}
		if(!StringUtils.isEmpty(condition.getOrderNo())) {
			hqlSb.append(" and orderNo like :orderNo");
			paramMap.put("orderNo", condition.getOrderNo()+"%");
		}
		if(!StringUtils.isEmpty(condition.getStatus())) {
			hqlSb.append(" and status = :status");
			paramMap.put("status", condition.getStatus());
		}
		if(null!=condition.getStatuses() && condition.getStatuses().length>0 ) {
			hqlSb.append(" and status in (:statuses)");
			paramMap.put("statuses", Arrays.asList(condition.getStatuses()));
		}
		countQl= "select count(o) "+hqlSb.toString();
		hqlSb.append(" order by updateTime ");
		
		Query countQuery = entityManager.createQuery(countQl);
		for(String key:paramMap.keySet()) {
			countQuery.setParameter(key, paramMap.get(key));
		}
		int totalCount = ((Number)countQuery.getSingleResult()).intValue();

		Query query = entityManager.createQuery(hqlSb.toString());
		for(String key:paramMap.keySet()) {
			query.setParameter(key, paramMap.get(key));
		}
		
		QueryResult<SubOrder> page = new QueryResult<SubOrder>(condition.getPageNo(),condition.getPageSize(),totalCount);
		
		query.setFirstResult((condition.getPageNo()-1)*condition.getPageSize());
		query.setMaxResults(condition.getPageSize());
		@SuppressWarnings("unchecked")
		List<SubOrder> results = query.getResultList();
		page.setResultList(results);
		
		return page;
	}
	
	public SubOrder getSubOrder(String orderNo) {
		return entityManager.find(SubOrder.class, orderNo);
	}
	
}
