package com.xiaoke1256.orders.product.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.common.exception.AppException;
import com.xiaoke1256.orders.product.assembler.ProductAssembler;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.entity.ProductEntity;
import com.xiaoke1256.orders.product.repository.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.bo.Product;
import com.xiaoke1256.orders.product.dao.ProductDao;
import com.xiaoke1256.orders.product.dto.SimpleProduct;

@Service
@Transactional
public class ProductService {

	public static final Logger LOG = LoggerFactory.getLogger(ProductService .class);

	@Autowired
	private IProductRepository productRepository;

	@Autowired
	private ProductDao productDao;
	
	/**
	 * 搜索商品
	 * @param condition 搜索条件
	 * @return 搜索结果
	 */
	@Transactional(readOnly=true)
	public QueryResult<com.xiaoke1256.orders.product.domain.Product> searchProductByCondition(ProductCondition condition){
		List<ProductEntity> pList = productRepository.queryByCondition(condition);
		QueryResult<com.xiaoke1256.orders.product.domain.Product> result = new QueryResult<>(condition.getPageNo(),condition.getPageSize(),condition.getTotal());
		result.setResultList(pList.stream().map((p)-> ProductAssembler.toDomain(p)).collect(Collectors.toList()));
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

	/**
	 * 上线或下线
	 * @param productCode 商品编号
	 * @param onOrOff 上架或下架
	 */
	public void switchOnShf(String productCode,String onOrOff){
		if("on".equalsIgnoreCase(onOrOff)){
		    onOrOff = "1";
		}else if("off".equalsIgnoreCase(onOrOff)){
			onOrOff = "0";
		}
		if(!Arrays.asList("1","0").contains(onOrOff)){
			throw new AppException(RespCode.WRONG_PARAMTER_ERROR.getCode(),"onOrOff 参数只能为 on 或 off");
		}
		Product p = new Product();
		p.setProductCode(productCode);
		p.setProductStatus(onOrOff);
		p.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		productDao.updateBySelective(p);
	}

	/**
	 * 增加库存
	 */
	public void addStorage(String productCode, int incNum){

	}
}
