package com.xiaoke1256.orders.search.controller;

import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SearchController {

	private Client client;


	
	public void search(){
		
	}
	
	public void distory() {
		client.close();
	}
}
