package com.xiaoke1256.orders.common.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;


@Repository
public class BaseDao {
	@PersistenceContext(unitName="default")
	private EntityManager entityManager ;
	
	public void persist(Object obj){
		entityManager.persist(obj);
	}
	
	public void merge(Object obj){
		entityManager.merge(obj);
	}
	
	public <T> T find(Class<T> clazz,Object id){
		return entityManager.find(clazz, id);
	}
	
	public void lock(Object obj ,LockModeType type){
		entityManager.lock(obj, type);
	}
	
	public <T> T find(Class<T> clazz,Object id,LockModeType type){
		return entityManager.find(clazz, id,type);
	}
	
	public List<?> queryByHql(String hql){
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}
	
	public void flush(){
		entityManager.flush();
	}
	

}
