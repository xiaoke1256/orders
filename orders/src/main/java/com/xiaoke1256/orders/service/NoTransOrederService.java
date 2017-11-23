package com.xiaoke1256.orders.service;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoTransOrederService {
	private static  final Logger logger = Logger.getLogger(OrederService.class);
	
	@PersistenceContext(unitName="default")
	private EntityManager entityManager;
	
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean modiyProductStore(String productId,Integer num){
//		Product product = entityManager.find(Product.class, productId);
//		if(product.getStockNum()<num)
//			return false;
//		product.setStockNum(product.getStockNum()-num);
//		entityManager.merge(product);
//		return true;
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
			conn = session.connection();
			conn.setAutoCommit(true);
			ps = conn.prepareStatement(" update PRODUCT set STOCK_NUM = (STOCK_NUM- ?) where PRODUCT_ID = ? and STOCK_NUM>= ?");
			ps.setLong(1, num.longValue());
			ps.setString(2, productId);
			ps.setLong(3, num.longValue());
			int result = ps.executeUpdate();
			return result>0;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
		
//			return (entityManager.createNativeQuery(" update PRODUCT set STOCK_NUM = (STOCK_NUM- ?) where PRODUCT_ID = ? and STOCK_NUM>= ? ")
//			.setParameter(1, num.longValue())
//			.setParameter(2, productId)
//			.setParameter(3, num.longValue())
//			.executeUpdate())
//				>0;
		
//		return (entityManager.createQuery(" update Product set stockNum = (stockNum- ?) where productId = ? and stockNum>= ? ")
//				.setParameter(1, num.longValue())
//				.setParameter(2, productId)
//				.setParameter(3, num.longValue())
//				.executeUpdate())
//					>0;
	}
	
	public boolean modiyProductStoreWithTrans(String productId,Integer num){
		return (entityManager.createQuery(" update Product set stockNum = (stockNum- ?) where productCode = ? and stockNum>= ? ")
			.setParameter(1, num.longValue())
			.setParameter(2, productId)
			.setParameter(3, num.longValue())
			.executeUpdate())
				>0;
	}
}
