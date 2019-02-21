package com.xiaoke1256.orders.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dao.ProductDao;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;

@Service
@Transactional
public class ProductService {
	@Autowired
	private ProductDao productDao;
	
	/**
	 * 搜索商品
	 * @param condition
	 * @return
	 */
	@Transactional(readOnly=true)
	public QueryResult<Product> searchProductByCondition(ProductCondition condition){
		Integer count = productDao.countByCondition(condition);
		if(count==null)
			count=0;
		QueryResult<Product> result = new QueryResult<Product>(condition.getPageNo(),condition.getPageSize(),count);
		List<Product> pList = productDao.queryByCondition(condition);
		result.setResultList(pList);
		return result;
	}
	
	@Transactional(readOnly=true)
	public Product getProductByCode(String productCode) {
		return productDao.getProductByCode(productCode);
	}
	
	@Transactional(readOnly=true)
	public SimpleProduct getSimpleProductByCode(String productCode) {
		return productDao.getSimpleProductByCode(productCode);
	}

	/**
	 * 开启秒杀活动
	 * @param productCode
	 */
	public void openSecKill(String productCode) {
		productDao.updateSecKill(productCode, "1");
	}

	/**
	 * 关闭秒杀活动
	 * @param productCode
	 */
	public void closeSecKill(String productCode) {
		productDao.updateSecKill(productCode, "0");
	}
}
