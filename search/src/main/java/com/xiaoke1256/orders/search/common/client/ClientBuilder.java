package com.xiaoke1256.orders.search.common.client;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientBuilder {
	@Autowired
	private ClientFactory clientFactory;
	
	@Bean
	public Client esClient() {
		return clientFactory.create();
	}
}
