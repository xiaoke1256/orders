package com.xiaoke1256.orders.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.bo.Product;
import com.xiaoke1256.orders.utils.RedisUtils;

import redis.clients.jedis.Jedis;

@Service
@Transactional
public class ProductService {
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	public Product getProduct(String productCode) {
		return entityManager.find(Product.class, productCode);
	}
	
	@Transactional(readOnly=true)
	public List<Product> queryProductsWithLimit(int limit) {
		return entityManager.createQuery("from Product").setMaxResults(limit).getResultList();
	}
	
	public void openSecKill(String productCode) {
		Jedis conn = RedisUtils.connect();
		Product product = entityManager.find(Product.class, productCode);
		if("1".equals(product.getInSeckill())) {
			//has open?
			return;
		}
		
		if(product.getStockNum()<100) {
			throw new RuntimeException(""+product.getProductCode()+"的库存不足,库存需大于100份才可支持秒杀活动。");
		}
		
		
		RedisUtils.set(conn, "SecKill_P_"+product.getProductCode(), String.valueOf(product.getStockNum()));
		
		product.setInSeckill("1");
		entityManager.merge(product);
		conn.close();
	}
	
	public void closeSecKill(String productCode) {
		Product product = entityManager.find(Product.class, productCode);
		
		Jedis conn = RedisUtils.connect();
		RedisUtils.del(conn, "SecKill_P_"+product.getProductCode());
		
		product.setInSeckill("0");
		entityManager.merge(product);
		conn.close();
	}
}
