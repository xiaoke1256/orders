package com.xiaoke1256.orders.product.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.product.dto.Product;
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
		BeanUtils.copyProperties(product, dto);
		return dto;
	}
}
