package com.xiaoke1256.orders.search.controller;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SearchController {

	@Autowired
	private Client client;

	@RequestMapping("/search")
	public void search(){
		
	}
	
	@PreDestroy
	public void distory() {
		client.close();
	}
}
