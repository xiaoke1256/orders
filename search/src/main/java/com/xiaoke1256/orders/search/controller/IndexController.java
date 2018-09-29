package com.xiaoke1256.orders.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * toIndex
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping("/product_search.htm")
	public String toProductSearch() {
		return "product_search";
	}
	
	
}
