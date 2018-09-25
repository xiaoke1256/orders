package com.xiaoke1256.orders.search.common.client;

import org.elasticsearch.client.Client;

public class ClientBuilder {
	private static class StaticHolder{
		static final Client INSTANCE = ClientFactory.create();
	}
	
	public static Client getSingleton() {
		return StaticHolder.INSTANCE;
	}
}
