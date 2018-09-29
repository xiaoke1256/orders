package com.xiaoke1256.orders.search.controller;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.search.vo.SearchCondition;
import com.xiaoke1256.orders.search.vo.SearchResult;

@RestController
@RequestMapping("/")
public class SearchController {

	@Autowired
	private Client client;

	@RequestMapping("/search")
	public SearchResult search(SearchCondition condition){
		return null;
	}
	
	@PreDestroy
	public void distory() {
		client.close();
	}
}
