package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.dto.ProductType;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import com.xiaoke1256.orders.product.dto.ProductParam;
import com.xiaoke1256.orders.product.api.ProductQueryService;
import com.xiaoke1256.orders.product.dto.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.ProductService;

@RestController
public class ProductController implements ProductQueryService {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("/product/{productCode}")
	public Product getProductByCode(@PathVariable String productCode) {
		logger.info("full product.");
		com.xiaoke1256.orders.product.bo.Product product = productService.getProductByCode(productCode);
		if(product==null)
			return null;
		Product dto = new Product();
		copyProperties(dto,product);
		return dto;
	}
	
	@GetMapping("/simpleProduct/{productCode}")
	public SimpleProduct getSimpleProductByCode(@PathVariable String productCode) {
		logger.info("simple product.");
		return productService.getSimpleProductByCode(productCode);
	}
	
	@RequestMapping(value="/product/search",method=RequestMethod.GET,consumes = "application/json")
	public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition){
		try {
			QueryResult<com.xiaoke1256.orders.product.bo.Product> result = productService.searchProductByCondition(condition);
			ArrayList<SimpleProduct> dtoList = new ArrayList<SimpleProduct>();
			for(com.xiaoke1256.orders.product.bo.Product product:result.getResultList()) {
				SimpleProduct dto = new SimpleProduct();
				copyProperties(dto,product,condition.isNeedFullTypeName());
				dtoList.add(dto);
			}
			QueryResult<SimpleProduct> newRet = new QueryResult<SimpleProduct>();
			newRet.setPageNo(result.getPageNo());
			newRet.setPageSize(result.getPageSize());
			newRet.setTotalCount(result.getTotalCount());
			newRet.setTotalPages(result.getTotalPages());
			newRet.setResultList(dtoList);
			return new SimpleProductQueryResultResp(newRet);
		}catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new SimpleProductQueryResultResp("99",ex.getMessage(),null);
		}
	}
	
	private void copyProperties(Product dto,com.xiaoke1256.orders.product.bo.Product product) {
		BeanUtils.copyProperties(product, dto);
		
		Store store = new Store();
		BeanUtils.copyProperties(product.getStore(), store);
		dto.setStore(store);
		
		List<ProductType> types = new ArrayList<ProductType>();
		if(product.getProductTypes()!=null)
			for(com.xiaoke1256.orders.product.bo.ProductType productType:product.getProductTypes()) {
				ProductType typeDto = new ProductType();
				copyProductType(typeDto,productType);
				types.add(typeDto);
			}
		dto.setProductTypes(types);
		
		ArrayList<ProductParam> paramDtos = new ArrayList<ProductParam>();
		if(product.getParams()!=null)
			for(com.xiaoke1256.orders.product.bo.ProductParam param:product.getParams()) {
				ProductParam paramDto = new ProductParam();
				BeanUtils.copyProperties(param,paramDto);
				paramDtos.add(paramDto);
			}
		dto.setParams(paramDtos);
	}
	
	private void copyProperties(SimpleProduct dto,com.xiaoke1256.orders.product.bo.Product product,boolean needFullTypeName) {
		BeanUtils.copyProperties(product, dto);
		if(product.getStore()!=null) {
			dto.setStoreName(product.getStore().getStoreName());
		}
		if(needFullTypeName && product.getProductTypes()!=null ) {
			StringBuilder typeSb = new StringBuilder();
			for(com.xiaoke1256.orders.product.bo.ProductType productType:product.getProductTypes()) {
				if(typeSb.length()==0)
					typeSb.append(" ").append(getFullTypeName(productType));
				
			}
			dto.setFullProductTypeName(typeSb.toString());
		}
	}
	
	private String getFullTypeName(com.xiaoke1256.orders.product.bo.ProductType type) {
		StringBuilder typeSb = new StringBuilder();
		if(type.getParentType()!=null) {
			typeSb.append( getFullTypeName(type.getParentType()));
			typeSb.append(" > ");
		}
		typeSb.append(type.getTypeName());
		return typeSb.toString();
	}
	
	private void copyProductType(ProductType dto,com.xiaoke1256.orders.product.bo.ProductType bo) {
		if(bo.getParentType()!=null) {
			ProductType parentDto = new ProductType();
			BeanUtils.copyProperties(bo.getParentType(), parentDto);
			copyProductType(parentDto,bo.getParentType());
			dto.setParentType(parentDto);
		}else {
			dto.setParentType(null);
		}
	}
}
