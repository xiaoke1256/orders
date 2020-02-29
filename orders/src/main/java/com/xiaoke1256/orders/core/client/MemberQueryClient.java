package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=ProductQueryFallbackFactory.class,path="product")
public interface MemberQueryClient extends MemberQueryService {
	
	@GetMapping("/members")
	public List<Member> findAll();
}
