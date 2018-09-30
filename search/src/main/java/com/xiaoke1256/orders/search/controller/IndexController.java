package com.xiaoke1256.orders.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * toIndex
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping(value="/product_search.htm",method={RequestMethod.GET})
	public String toProductSearch() {
		return "product_search";
	}
	
	
}
