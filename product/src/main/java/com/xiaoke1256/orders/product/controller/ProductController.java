package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.dto.ProductType;
import com.xiaoke1256.orders.product.dto.ProductParam;
import com.xiaoke1256.orders.product.dto.Product;
import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.ProductQueryResult;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.ProductService;

@RestController
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@GetMapping("/product/{productCode}")
	public Product getProductByCode(@PathVariable String productCode) {
		com.xiaoke1256.orders.product.bo.Product product = productService.getProductByCode(productCode);
		if(product==null)
			return null;
		Product dto = new Product();
		copyProperties(dto,product);
		return dto;
	}
	
	@GetMapping("/product/search")
	public ProductQueryResult searchProductByCondition(ProductCondition condition){
		QueryResult<com.xiaoke1256.orders.product.bo.Product> result = productService.searchProductByCondition(condition);
		ArrayList<Product> dtoList = new ArrayList<Product>();
		for(com.xiaoke1256.orders.product.bo.Product product:result.getResultList()) {
			Product dto = new Product();
			copyProperties(dto,product);
			dtoList.add(dto);
		}
		ProductQueryResult newRet = new ProductQueryResult();
		newRet.setPageNo(result.getPageNo());
		newRet.setPageSize(result.getPageSize());
		newRet.setTotalCount(result.getTotalCount());
		newRet.setTotalPages(result.getTotalPages());
		newRet.setResultList(dtoList);
		return newRet;
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
