package com.xiaoke1256.orders.search.common.client;

import java.net.InetAddress;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/elasticsearch-config.properties")
public class ClientFactory {
	@Value("${orders-search.es.cluster.name}") 
	private String clusterName;
	@Value("${orders-search.es.hostname}")
	private String networkHost;
	@Value("${orders-search.es.port}")
	private String networkPort;
	@Value("${orders-search.es.client.transport.sniff}")
	private String transportSniff;
	
	public Client create() {
		try {
			if(StringUtils.isEmpty(clusterName))
				throw new NullPointerException("ClusterName can not be empty");
			if(StringUtils.isEmpty(networkHost))
				throw new NullPointerException("NetworkHost can not be empty");
			if(StringUtils.isEmpty(networkPort))
				throw new NullPointerException("networkPort can not be empty");
			Settings settings = Settings.builder().put("cluster.name",clusterName)
					.put("client.transport.sniff", StringUtils.isNotEmpty(transportSniff)?Boolean.getBoolean(transportSniff):false)
					.build();
			@SuppressWarnings("resource")
			TransportClient client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(networkHost),Integer.valueOf(networkPort)));
			return client;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
