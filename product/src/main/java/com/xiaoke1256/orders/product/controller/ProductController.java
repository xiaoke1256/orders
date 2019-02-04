package com.xiaoke1256.orders.product.controller;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.product.dto.Product;
import com.xiaoke1256.orders.product.dto.ProductQueryResult;
import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.service.ProductService;
import com.xiaoke1256.orders.product.vo.ProductCondition;

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
		BeanUtils.copyProperties(product, dto);
		//Store 特殊处理一下。
		Store store = new Store();
		BeanUtils.copyProperties(product.getStore(), store);
		dto.setStore(store);
		return dto;
	}
	
	@GetMapping("/product/search")
	public ProductQueryResult searchProductByCondition(ProductCondition condition){
		QueryResult<com.xiaoke1256.orders.product.bo.Product> result = productService.searchProductByCondition(condition);
		ArrayList<Product> dtoList = new ArrayList<Product>();
		for(com.xiaoke1256.orders.product.bo.Product product:result.getResultList()) {
			Product dto = new Product();
			BeanUtils.copyProperties(product, dto);
			//Store 特殊处理一下。
			Store store = new Store();
			BeanUtils.copyProperties(product.getStore(), store);
			dto.setStore(store);
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
}
